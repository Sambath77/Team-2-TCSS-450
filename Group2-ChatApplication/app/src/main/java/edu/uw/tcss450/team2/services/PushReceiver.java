package edu.uw.tcss450.team2.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import edu.uw.tcss450.team2.AuthActivity;
import edu.uw.tcss450.team2.MainActivity;
import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.chat.ChatFragment;
import edu.uw.tcss450.team2.chat.ChatMessage;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";

    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {

        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String typeOfMessage = intent.getStringExtra("type");
        System.out.println("Type of Message: " + typeOfMessage);
        ChatMessage message = null;
        int chatId = -1;
        String returnedMessage = null;
        boolean flag = false;

        System.out.println("Context -> " + context);

        if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("NewMessage")) {
            try{
                message = ChatMessage.createFromJsonString(intent.getStringExtra("message"));
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }
        }
        /*TODO implementing add Friend possibly wrong*/
        //create new chat room
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("CreateNewChatRoom"))){
            System.out.println("CreateNewChatRoom Initiated");
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);
            flag = true;
        }
        //remove member
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("RemoveMemberToChatRoom"))){
            System.out.println("RemoveMemberToChatRoom");
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);
            flag = true;
        }
        //add member
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("AddMemberToChatRoom"))) {
            System.out.println("AddMemberToChatRoom");
            //TODO This is the message I would like to print
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);

            flag = true;
        }
        //Chatroom deleted
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("DeleteChatRoom"))) {
            System.out.println("DeleteChatRoom");
            //TODO This is the message I would like to print
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);

            flag = true;
        }
        //For dealing with Friend Request
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("SendingFriendRequest"))) {
            System.out.println("SendingFriendRequest");
            //TODO This is the message I would like to print
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);

            flag = true;
        }
        //Friend Request rejected
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("RejectFriendRequest"))) {
            System.out.println("RejectFriendRequest");
            //TODO This is the message I would like to print
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);

            flag = true;
        }

        //Friend request approved
        else if((typeOfMessage != null && typeOfMessage.equalsIgnoreCase("AcceptFriendRequest"))) {
            System.out.println("AcceptFriendRequest");
            //TODO This is the message I would like to print
            returnedMessage = intent.getStringExtra("message");

            System.out.println(returnedMessage);

            flag = true;
        }



        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "Message received in foreground: " + message);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("chatMessage", message);
            i.putExtra("chatid", chatId);
            i.putExtras(intent.getExtras());

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("AddMemberToChatRoom")) {
                i.putExtra("AddMemberToChatRoom", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("RemoveMemberToChatRoom")) {
                i.putExtra("RemoveMemberToChatRoom", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("CreateNewChatRoom")) {
                i.putExtra("CreateNewChatRoom", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("DeleteChatRoom")) {
                i.putExtra("DeleteChatRoom", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("SendingFriendRequest")) {
                i.putExtra("SendingFriendRequest", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("RejectFriendRequest")) {
                i.putExtra("RejectFriendRequest", "");
                i.putExtras(intent.getExtras());
            }

            if(typeOfMessage != null && typeOfMessage.equalsIgnoreCase("AcceptFriendRequest")) {
                i.putExtra("AcceptFriendRequest", "");
                i.putExtras(intent.getExtras());
            }

            context.sendBroadcast(i);

        } else {
            NotificationCompat.Builder builder = null;
            if(flag) {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + returnedMessage);

                //System.out.println("Test Message -> " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        //TODO Need to change this line
                        .setContentTitle("Social Network") //Name of our application
                        .setContentText(returnedMessage)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);
            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + message.getMessage());

                //System.out.println("Test Message -> " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        .setContentTitle("Message from: " + message.getSender())
                        .setContentText(message.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);
            }


            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }

    }


}
