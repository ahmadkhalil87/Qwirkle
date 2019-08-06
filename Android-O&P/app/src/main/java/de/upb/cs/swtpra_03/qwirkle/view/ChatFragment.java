package de.upb.cs.swtpra_03.qwirkle.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;

/**
 * Provide UI Related Functions used for the in-game chat
 */
public class ChatFragment extends Fragment {

    private EditText messageBox;
    private LinearLayout messageLog;
    private ScrollView messageLogScroll;
    private Button sendMessage;

    private OnChatFragmentListener listener;

    /**
     * Create a new View from Layout and bind functions to UI Elements
     *
     * @param inflater           Layout inflater
     * @param container          Parent View
     * @param savedInstanceState State
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create View from Layout
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Find Elements
        messageBox = view.findViewById(R.id.chat_message);
        sendMessage = view.findViewById(R.id.send_message);
        messageLog = view.findViewById(R.id.messageLog);
        messageLogScroll = (ScrollView) messageLog.getParent();

        // get InputMethodManager
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );

        // handle "Send"-Button Click
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = messageBox.getText().toString();
                listener.onClickSend(msg);

                // Clear Input
                messageBox.setText("");

                // Hide Keyboard
                imm.hideSoftInputFromWindow(messageBox.getWindowToken(), 0);
            }
        });

        return view;
    }

    /**
     * Updates chat with a new message.
     *
     * @param client    client who sent the message
     * @param msg       new Message to be displayed
     */
    public void updateChat(Client client, String msg){
        if (getActivity() == null){
            return;
        }

        getActivity().runOnUiThread(() -> {
            if(!isVisible()){
                return;
            }

            // prevent Empty messages
            if (!msg.equals("")) {

                View v = getLayoutInflater().from(getContext()).inflate(R.layout.scrollitem_chat_message, messageLog, false);
                TextView usernameView = v.findViewById(R.id.chat_sender);
                TextView messageView = v.findViewById(R.id.chat_message);

                usernameView.setText(client.getClientName());

                if(client.getClientType() == ClientType.PLAYER) {
                    usernameView.setTextColor(0xFF000000);
                }else if(client.getClientType() == ClientType.SPECTATOR) {
                    usernameView.setTextColor(0xFF333333);
                }
                messageView.setText(msg);

                // Attach view to Chat log
                messageLog.addView(v);

                // Scroll Chat to Bottom
                messageLogScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        messageLogScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }


    /**
     * Defines a method in parent for notifying it about a new chat message to send to server
     */
    public interface OnChatFragmentListener {
        public void onClickSend(String msg);
    }

    /**
     * Attaches this fragment to the life cycle of the parent activity.
     * Android intern method.
     * Used here to make sure parent activity uses our interface.
     *
     * @param context   parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatFragmentListener) {
            listener = (OnChatFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ChatFragment.OnChatFragmentListener");
        }
    }

    /**
     * Detaches this fragment from parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

