package nevicelabs.pibiti;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

/**
 * Esta classe service excuta as mensagens enviadas por dispositivos bluetooth no backgorund do app.
 * Quando uma mensagem é encontrada, o método onHandleIntent é chamado para lidar com seu conteúdo.
 */
public class MensagensService extends IntentService {

    private static final int MESSAGES_NOTIFICATION_ID = 1;

    // Construtuor sem argumentos apra a definição do Service em AndroidManifest.xml
    public MensagensService() {
        super("name");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MensagensService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("", "Serviço iniciado");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("","Serviço interrompido");
    }

    /**
     * Este método é chamado quando o aplicativo encontra uma mensagem enviada por um dispositivo.
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MessageListener listener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i("", "Mensagem encontrada: " + message);
                atualizarNotificacao(message);
            }

            @Override
            public void onLost(Message message) {
                super.onLost(message);
                Log.i("", "Fora do alcance da mensagem: " + message);
            }
        };
    }

    private void atualizarNotificacao(Message mensagem) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
        launchIntent.setAction(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        String titulo = mensagem.getType();
        String texto = new String(mensagem.getContent());
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(R.drawable.powered_by_google_dark);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(intent);

        notificationManager.notify(MESSAGES_NOTIFICATION_ID, notificacao.build());
    }
}
