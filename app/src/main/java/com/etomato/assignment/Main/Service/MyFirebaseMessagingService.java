package com.etomato.assignment.Main.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import com.etomato.assignment.Main.View.Fragment.WriteFragment;
import com.etomato.assignment.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * 참고: FCM 메시지를 수신하는 각 앱에는 하나의 서비스만 있을 수 있습니다. 다중인 경우
 * 매니페스트에 선언된 다음 첫 번째 항목이 선택됩니다.
 *
 * 이 Java 샘플이 작동하도록 하려면 Kotlin 메시징에서 다음을 제거해야 합니다.
 * AndroidManifest.xml의 서비스: *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * 메시지 수신 시 호출됩니다.
     *
     * @param remoteMessage 클라우드 메시징에서 받은 메시지를 나타내는 개체입니다.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // 메시지 데이터 메시지와 알림 메시지에는 두 가지 유형이 있습니다. 데이터 메시지
        // 처리
        // 앱이 포그라운드에 있는지 백그라운드에 있는지 여부는 onMessageReceived에 있습니다. 데이터
        // 메시지는 유형입니다.
        // 전통적으로 GCM과 함께 사용됩니다. 알림 메시지는 여기에서만 수신됩니다.
        // 앱에서 onMessageReceived
        // 전경에 있습니다. 앱이 백그라운드에 있을 때 자동으로 생성됨
        // 알림이 표시됩니다.
        // 사용자가 알림을 탭하면 앱으로 돌아갑니다. 메시지
        // 두 알림 모두 포함
        // 데이터 페이로드는 알림 메시지로 처리됩니다. Firebase 콘솔은 항상
        // 알림을 보냅니다.
        // 메시지. 자세한 내용은 https://firebase.google.com/docs/cloud-messaging/concept-options를 참조하세요.
        // [END_EXCLUDE]

        // TODO(developer): 여기에서 FCM 메시지를 처리합니다.
        // 여기에 메시지가 수신되지 않습니까? 이유를 확인하세요: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        sendNotification(remoteMessage.getNotification().getBody());

        // 메시지에 데이터 페이로드가 포함되어 있는지 확인합니다.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* 장기 실행 작업에서 데이터를 처리해야 하는지 확인 */ true) {
                // 장기 실행 작업(10초 이상)의 경우 WorkManager 를 사용합니다.
                scheduleJob();
            } else {
                // 10초 이내에 메시지 처리
                handleNow();
            }

        }

        // 메시지에 알림 페이로드가 포함되어 있는지 확인합니다.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // 또한 수신된 FCM의 결과로 자체 알림을 생성하려는 경우
        // 메시지, 여기에서 시작되어야 합니다. 아래의 sendNotification 메소드를 참조하십시오.
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * onNewToken이 호출되는 두 가지 시나리오가 있습니다.
     * 1) 최초 앱 시작 시 새로운 토큰이 생성되는 경우
     * 2) 기존 토큰이 변경될 때마다
     * #2에서는 기존 토큰이 변경되는 세 가지 시나리오가 있습니다.
     * 가) 앱이 새 기기로 복원됩니다.
     * 나) 사용자가 앱을 제거/재설치하는 경우
     * 다) 사용자가 앱 데이터 삭제
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // 이 애플리케이션 인스턴스에 메시지를 보내거나
        // 서버 측에서 이 앱 구독을 관리하고
        // 앱 서버에 대한 FCM 등록 토큰.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * WorkManager 를 사용하여 비동기 작업을 예약합니다.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * BroadcastReceivers 에 할당된 처리 시간.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * 타사 서버에 토큰을 유지합니다.
     *
     * 이 방법을 수정하여 사용자의 FCM 등록 토큰을 임의의
     * 응용 프로그램에서 유지 관리하는 서버 측 계정.
     *
     * @param token 새 토큰입니다.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * 수신된 FCM 메시지가 포함된 간단한 알림을 만들고 표시합니다.
     *
     * @param messageBody FCM 메시지 본문이 수신되었습니다.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, WriteFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_sun)
                        .setContentTitle("뉴스통")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android Oreo 알림 채널이 필요하기 때문에.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
