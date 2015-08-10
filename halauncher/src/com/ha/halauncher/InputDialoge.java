package com.ha.halauncher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class InputDialoge extends DialogFragment {
	
	InputListener m_listener;
	
	public InputDialoge() {
		
		
	}
	
	public void setListener(InputListener l){
		m_listener = l;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.input, null))
	    // Add action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   m_listener.onDialogPositiveClick(InputDialoge.this);
	               }
	           })
	           .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   InputDialoge.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}

}
