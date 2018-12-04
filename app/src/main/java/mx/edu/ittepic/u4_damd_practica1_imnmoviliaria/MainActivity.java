package mx.edu.ittepic.u4_damd_practica1_imnmoviliaria;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText identificador,nombre,ingredientes,preparacion,observaciones;
    Button insertar,consultar,actualizar,borrar;
    BaseDatos base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        identificador = findViewById(R.id.ide);
        nombre = findViewById(R.id.nombre);
        ingredientes = findViewById(R.id.ingredientes);
        preparacion = findViewById(R.id.preparacion);
        observaciones = findViewById(R.id.observaciones);

        insertar = findViewById(R.id.insertar);
        consultar = findViewById(R.id.consultar);
        actualizar = findViewById(R.id.actualizar);
        borrar = findViewById(R.id.borrar);

        base = new BaseDatos(this, "primera",null,1);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoinsertar();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirID(1);
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirID(2);

            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actualizar.getText().toString().startsWith("CONFIRMAR ACTUALIZACION")){
                    confirmacionactualizar();
                     return;


                }
                pedirID(3);
            }
        });
    }
    private void habilitarBotonesYLimpiarCampos(){
        identificador.setText("");
        nombre.setText("");
        ingredientes.setText("");
        preparacion.setText("");
        observaciones.setText("");
        insertar.setEnabled(true);
        consultar.setEnabled(true);
        borrar.setEnabled(true);
        actualizar.setText("ACTUALIZAR");
        identificador.setEnabled(true);
    }
    private void confirmacionactualizar() {
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
        confirmar.setTitle("ATENCION").setMessage("ESTAS SEGURO QUE DESEAS ACTUALIZAR EL REGISTRO")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarDato();
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                habilitarBotonesYLimpiarCampos();
                dialog.cancel();
            }
        }).show();

    }

    private void actualizarDato() {
        try{
            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL = "UPDATE RECETAS SET NOMBRE='"+nombre.getText().toString()+"', INGREDIENTES='"+ingredientes.getText().toString()+"', PREPARACION='"+preparacion.getText().toString()+"', OBSERVACIONES='"+observaciones.getText().toString()+"' WHERE ID="+identificador.getText().toString();
            tabla.execSQL(SQL);
            tabla.close();
            Toast.makeText(MainActivity.this, "SE ACTUALIZO", Toast.LENGTH_LONG).show();
        }catch (SQLiteException e){
            Toast.makeText(MainActivity.this, "NO SE PUDO ACTULIZAR", Toast.LENGTH_LONG).show();
        }
        habilitarBotonesYLimpiarCampos();
    }

    private void pedirID(final int origen) {

        final EditText pidoID = new EditText(this);
        String mensaje ="", mensajeButton = null;
        pidoID.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(origen==1){
            mensaje = "ESCRIBE ID A BUSCAR";
            mensajeButton = "BUSCAR";
        }

        if(origen==2){
            mensaje = "ESCRIBE EL ID A ELIMINAR";
            mensajeButton = "ELIMINAR";
        }

        if(origen==3){
            mensaje= "ESCRIBE EL ID A MODIFICAR";
            mensajeButton = "MODIFICAR";
        }

        pidoID.setHint(mensaje);

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("ATENCIÓN").setMessage(mensaje)
                .setView(pidoID)
                .setPositiveButton(mensajeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pidoID.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this,"DEBES ESCRIBIR VALOR", Toast.LENGTH_LONG).show();
                            return;
                        }
                        buscardato(pidoID.getText().toString(), origen);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR",null).show();
    }


    private void eliminarDato(String idborrar) {
        try {


            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL = "DELETE FROM RECETAS WHERE ID=" + idborrar;
            tabla.execSQL(SQL);
            Toast.makeText(MainActivity.this, "SE ELIMINO CORRECTAMENTE EL REGISTRO", Toast.LENGTH_LONG).show();
            limpiar();
            tabla.close();
        }catch (SQLiteException e){
            Toast.makeText(MainActivity.this, "NO SE ELIMINO EL REGISTRO", Toast.LENGTH_LONG).show();
        }
    }

    private void limpiar (){
        identificador.setText("");
        nombre.setText("");
        ingredientes.setText("");
        preparacion.setText("");
        observaciones.setText("");

    }

    private void confirmarborrar (String datos) {

        String[] cadena = datos.split("&");
        final String id =cadena[0];
        String nombre = cadena[1];
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
        confirmar.setTitle("ATENCION").setMessage("ESTAS SEGURO QUE DESEAS BORRAR EL REGISTRO" + nombre +"?")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarDato(id);
                        limpiar();
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpiar();
               dialog.cancel();
            }
        }).show();
    }




    private void buscardato (String idABuscar, int origen){
        try {
            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL = "SELECT * FROM RECETAS WHERE ID="+idABuscar;

            Cursor resultado = tabla.rawQuery(SQL,null);
            if(resultado.moveToFirst()){
                //resultado
                identificador.setText(resultado.getString(0));
                nombre.setText(resultado.getString(1));
                ingredientes.setText(resultado.getString(2));
                preparacion.setText(resultado.getString(3));
                observaciones.setText(resultado.getString(4));

                if(origen==2){
                    //Esto siginifica que el resultó  para borrar
                    String datos = idABuscar+"&"+resultado.getString(1)+"&"+resultado.getString(2)+"&"+resultado.getString(3)+"&"+resultado.getString(4);
                    confirmarborrar(datos);
                    return;
                }

                if(origen==3){
                    //modificar
                    insertar.setEnabled(false);
                    consultar.setEnabled(false);
                    borrar.setEnabled(false);
                    actualizar.setText("CONFIRMAR ACTUALIZACION");
                    identificador.setEnabled(false);
                }
                Toast.makeText(this,"SI SE ENCONTRO RESULTADO",Toast.LENGTH_LONG).show();
            }else {
                //no hay resultado
                Toast.makeText(this,"ERROR: NO SE PUDO ENCONTRAR",Toast.LENGTH_LONG).show();
            }
            tabla.close();
        }catch (SQLiteException e){
            Toast.makeText(this,"ERROR: NO SE PUDO ENCONTRAR",Toast.LENGTH_LONG).show();
        }

    }
    private void codigoinsertar() {

        try{
            SQLiteDatabase tabla = base.getWritableDatabase();

            String SQL = "INSERT INTO RECETAS VALUES(1,'%2','%3','%4','%5') ";
            SQL =SQL.replace("1",identificador.getText().toString());
            SQL = SQL.replace("%2",nombre.getText().toString());
            SQL = SQL.replace("%3",ingredientes.getText().toString());
            SQL = SQL.replace("%4",preparacion.getText().toString());
            SQL = SQL.replace("%5",observaciones.getText().toString());

            tabla.execSQL(SQL);
            limpiar();
            Toast.makeText(this,"SI SE PUDO INSERTAR",Toast.LENGTH_LONG).show();

        }catch (SQLiteException e){
            Toast.makeText(this,"ERROR: NO SE PUDO INSERTAR",Toast.LENGTH_LONG).show();
        }

    }
}
