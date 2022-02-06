package com.example.churm.lectorlibro.fragments;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.churm.lectorlibro.Aplicacion;
import com.example.churm.lectorlibro.Libro;
import com.example.churm.lectorlibro.MainActivity;
import com.example.churm.lectorlibro.MyService;
import com.example.churm.lectorlibro.R;
public class DetalleFragment extends Fragment implements View.OnTouchListener{
    public final String NotiChanelID = "audioLibros2";
    public static String ARG_ID_LIBRO = "id_libro";


//    MediaController mediaController;
    MyService servicio;
    boolean mBound = false;
    int libroID;

    @Override public View onCreateView(LayoutInflater inflador, ViewGroup
            contenedor, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity().getApplicationContext(),MyService.class);
        getActivity().bindService(intent,connection, Context.BIND_AUTO_CREATE);
        Libro libro;
        View vista = inflador.inflate( R.layout.fragment_detalle,
                contenedor, false);
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            libroID =args.getInt(ARG_ID_LIBRO);
            libro = ((Aplicacion) getActivity().getApplication())
                    .getVectorLibros().elementAt(position);
            ponInfoLibro(position, vista);
        } else {
            libroID=0;
            ponInfoLibro(0, vista);
            libro = ((Aplicacion) getActivity().getApplication())
                    .getVectorLibros().elementAt(0);
        }
        return vista;
    }

    private void ponInfoLibro(int id, View vista) {
        Libro libro = ((Aplicacion) getActivity().getApplication())
                .getVectorLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada))
                .setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }

    @Override public boolean onTouch(View vista, MotionEvent evento) {
        try {
            if (servicio.getMediaPlayer()!=null){
                if (servicio.libroID != this.libroID){
                    if(libroID>=0){
                        NotificationCompat.Builder noti = new NotificationCompat.Builder(getActivity().getApplicationContext(),
                                NotiChanelID).setContentTitle("AudioLibros").setContentText("Reproduciendo Audio").setPriority(NotificationCompat.PRIORITY_LOW);
                        Intent intent = new Intent(getActivity().getApplicationContext(),MyService.class);
                        servicio.startForeground(0,noti.build());
                        Libro libro = ((Aplicacion) getActivity().getApplication())
                                .getVectorLibros().elementAt(libroID);
                        servicio.setMediaPlayer(getActivity(),libro,this.getView(),libroID);
                    }
                }else {
                    servicio.showMedia();
                }
            }else{
                if(libroID>=0){
                    NotificationCompat.Builder noti = new NotificationCompat.Builder(getActivity().getApplicationContext(),NotiChanelID).setContentTitle("AudioLibros").setContentText("Reproducionedo Audio").setPriority(NotificationCompat.PRIORITY_LOW);
                    servicio.startForeground(0,noti.build());
                    Intent intent = new Intent(getActivity().getApplicationContext(),MyService.class);
                    servicio.startForeground(0,new Notification());
                    Libro libro = ((Aplicacion) getActivity().getApplication())
                            .getVectorLibros().elementAt(libroID);
                    servicio.setMediaPlayer(getActivity(),libro,this.getView(),libroID);
                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), "algo salió mal \n" +e, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override public void onStop() {
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        if (!preferencias.getBoolean("pref_autoreproducir", true)) {
            try {
                servicio.stopping();
            }catch (Exception e){
            }
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        servicio.stopSelf();
        super.onDestroy();
    }

    @Override public void onResume(){
        DetalleFragment detalleFragment = (DetalleFragment)
                getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null ) {
            ((MainActivity) getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.miBinder binder = (MyService.miBinder) service;
            servicio = binder.getService();
            mBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound=false;
        }
    };
}