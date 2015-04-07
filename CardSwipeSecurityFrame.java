/*
CardSwipeSecurityFrame.java
By Reed McCartney
April 7, 2015

For use with CardSwipeSecurity.java

This is the GUI for the program.
- Contains
Constructor: 	CardSwipeSecurityFrame()
Method: 	loadLogFromRecords() - fills in the security log with records from database
Event Method:	actionPerformed() - responds to ActionEvents
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

public class CardSwipeSecurityFrame extends JFrame implements ActionListener
{
	// Useful Strings most likely to change:
	private static final String title = "FFB Access Log";
	private String[] locations = {"Managers Office","Front Desk","Vault","Counting Room"};
	
	// Tool
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	
	// Declaring Components for class-wide access
	private JTextArea txaLog;
	private JScrollPane scrLog;
	private JTextField txfCardEntry;
	private JComboBox<String> cboLocationSelect;
	private JButton btnSwipeCard;
	private JLabel lblUserError;
	
	// Constructor for the GUI
	public CardSwipeSecurityFrame()
	{
		super(title);
		
		this.setSize(640,480);
		this.setLayout(new CardLayout()); //Card Layout expands my options for the future.
		Container content = this.getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Layout
		JPanel pnlMain = new JPanel();
		JPanel pnlLog = new JPanel();
		JPanel pnlCard = new JPanel();
		JPanel pnlLocation = new JPanel();
		JPanel pnlButton = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.PAGE_AXIS));
		pnlLog.setLayout(new BoxLayout(pnlLog, BoxLayout.PAGE_AXIS));
		pnlCard.setLayout(new BoxLayout(pnlCard, BoxLayout.LINE_AXIS));
		pnlLocation.setLayout(new BoxLayout(pnlLocation, BoxLayout.LINE_AXIS));
		pnlButton.setLayout(new BoxLayout(pnlButton, BoxLayout.LINE_AXIS));
		
		// Initializing Components
		JLabel lblLog = new JLabel("Access Log");
		 txaLog = new JTextArea("",18,50);
		 scrLog = new JScrollPane(txaLog);
		JLabel lblCardPrompt = new JLabel("Card ID #");
		 txfCardEntry = new JTextField(15);
		JLabel lblLocationPrompt = new JLabel("Location");
		cboLocationSelect = new JComboBox<>(locations);
		btnSwipeCard = new JButton("Swipe Card");
		lblUserError = new JLabel("");
		
		// More layout, assigning behavioral properties and listeners.
		lblLog.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		txaLog.setEditable(false);
		btnSwipeCard.addActionListener(this);
		txfCardEntry.addActionListener(this);
		
		pnlMain.setPreferredSize(new Dimension(640,440));
		
		pnlLog.add(lblLog);
		pnlLog.add(Box.createRigidArea(new Dimension(0,5)));
		pnlLog.add(scrLog);
		pnlLog.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		pnlCard.add(lblCardPrompt);
		pnlCard.add(Box.createRigidArea(new Dimension(5,0)));
		pnlCard.add(txfCardEntry);
		pnlCard.add(Box.createHorizontalGlue());
		pnlCard.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		pnlLocation.add(lblLocationPrompt);
		pnlLocation.add(Box.createRigidArea(new Dimension(5,0)));
		pnlLocation.add(cboLocationSelect);
		pnlLocation.add(Box.createHorizontalGlue());
		pnlLocation.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		pnlButton.add(btnSwipeCard);
		pnlButton.add(Box.createRigidArea(new Dimension(5,0)));
		pnlButton.add(lblUserError);
		pnlButton.add(Box.createHorizontalGlue());
		pnlButton.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		pnlMain.add(pnlLog);
		pnlMain.add(pnlCard);
		pnlMain.add(pnlLocation);
		pnlMain.add(pnlButton);
		content.add(pnlMain);
		
		// Add previous records from the database to the Log.
		loadLogFromRecords();
	}
	
	public void loadLogFromRecords()
	{
		int count = CardSwipeSecurity.logTime.size();
		Calendar calTime = Calendar.getInstance();
		
		if (count > 0)
		{
			for (int x = 0; x < count; ++x)
			{
				calTime.setTimeInMillis(CardSwipeSecurity.logTime.get(x));
				txaLog.append("ID " + CardSwipeSecurity.logID.get(x) + " entered " +
					CardSwipeSecurity.logLocation.get(x) + " at " + 
					sdf.format(calTime.getTime()) + ". " + System.lineSeparator());
			}
		}
		else
			lblUserError.setText("Log empty.");
	}
	
	// Action for when the button is clicked
	public void actionPerformed(ActionEvent e)
	{
		Calendar calTime = Calendar.getInstance();
		Long time = calTime.getTimeInMillis();
		String id;
		String loc;
		
		lblUserError.setText("");
		
		if (txfCardEntry.getText().equals(null) || txfCardEntry.getText().equals(""))
			lblUserError.setText("Please enter an ID");
		else
		{
			id = txfCardEntry.getText();
			loc = (String)cboLocationSelect.getSelectedItem();
			
			txaLog.append("ID " + id + " entered " + loc + 
				" at " + sdf.format(calTime.getTime()) + ". " + 
				System.lineSeparator());
			
			CardSwipeSecurity.writeLogRecord(time, id, loc);
		}
	}
}
