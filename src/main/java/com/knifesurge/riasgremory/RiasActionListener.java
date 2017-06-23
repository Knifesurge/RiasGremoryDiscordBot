package com.knifesurge.riasgremory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RiasActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		if(action.equalsIgnoreCase("send"))
		{
			String cmd = RiasGremoryListener.input.getText();
			RiasGremoryListener.sendMessage(new MessageReceivedEvent(RiasGremoryBot.jda, 0, RiasGremoryListener.msg), cmd);
		}
	}
}
