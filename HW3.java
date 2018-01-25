package gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;

public class HW3 extends JFrame {

	private JPanel mainPane;
	List<JCheckBox> cbox=new ArrayList<JCheckBox>();
	List<JCheckBox> subCbox=new ArrayList<JCheckBox>();
	List<JCheckBox> attriBox=new ArrayList<JCheckBox>();
	SubCatPanel subCatPanel;
	AttributePanel attributePanel;
	JScrollPane scrollPane_1;
	JScrollPane scrollPane_2;
	JScrollPane scrollPane_3;
	JScrollPane tablePane;
	JTable table;
	JPanel panel_6;
	JPanel panel_1;
	JPanel panel_2;
	JPanel panel_3;
	DropDownPanel dropdownPanel;
	JComboBox location;
	JComboBox dayCB;
	JComboBox fromCB;
	JComboBox toCB;
	public static String decide="ANY";
	JComboBox decideCB;
	
	public static List<String> selectMain=new ArrayList();
	public static List<String> selectSub=new ArrayList();
	public static List<String> selectAttri=new ArrayList();
	public static String selectLoc="-SELECT-";
	public static String selectDay="-SELECT-";
	public static String selectFrom="-SELECT-";
	public static String selectTo="-SELECT-";
	SearchPanel searchPanel;
	ResultPanel resultPanel;
	private JPanel searchFor;
	private JLabel lblPleaseSetThe;
	private JComboBox comboBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{	HW3 frame = new HW3();
					//popPreference(frame);
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public HW3() throws SQLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\vaish\\Desktop\\yelplogo.png"));
		setTitle("Welcome to Yelp Application");
		initComponents();
		createEvents();
		
	}
///////////////////////////////////////////////////////////////////////////////////////////
////	This method contains all of the code for creating events
////////////////////////////////////////////////////////////////////////////////////////////
	private void createEvents() 
	{
		// TODO Auto-generated method stub
		
	}
///////////////////////////////////////////////////////////////////////////////////////////
////This method contains all of the code for creating and
//initialize components
////////////////////////////////////////////////////////////////////////////////////////////
	private void initComponents() throws SQLException 
	{
		// TODO Auto-generated method stub
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		mainPane.add(panel_5, BorderLayout.PAGE_END);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		dropdownPanel = new DropDownPanel();
		dropdownPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Advanced Filter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.add(dropdownPanel);
		dropdownPanel.setLayout(new GridLayout(2, 5, 0, 0));
		
		JLabel loclabel = new JLabel("Location");
		dropdownPanel.add(loclabel);
		
		JLabel dayLabel = new JLabel("Day");
		dropdownPanel.add(dayLabel);
		
		JLabel fromLabel = new JLabel("From");
		dropdownPanel.add(fromLabel);
		
		JLabel toLabel = new JLabel("To");
		dropdownPanel.add(toLabel);
		
		location = new JComboBox();
		dropdownPanel.add(location);
		
		
		dayCB = new JComboBox();
		dropdownPanel.add(dayCB);
		
		
		fromCB = new JComboBox();
		dropdownPanel.add(fromCB);
		
		toCB = new JComboBox();
		dropdownPanel.add(toCB);
		
		String search[]={"ANY","ALL"};
		
		searchPanel = new SearchPanel();
		searchPanel.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(0, 0, 0)));
		panel_5.add(searchPanel);
		
		
		JButton searchBtn = new JButton("SEARCH");
		searchPanel.add(searchBtn);
		
		JButton btnNewButton_1 = new JButton("CLOSE");
		searchPanel.add(btnNewButton_1);
		
		panel_6 = new JPanel();
		mainPane.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel filterPanel = new JPanel();
		panel_6.add(filterPanel);
		filterPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		filterPanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		MainCatPanel mainCatPanel = new MainCatPanel();
		mainCatPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)), "Main Category", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		filterPanel.add(mainCatPanel);
		mainCatPanel.setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panel);
		mainCatPanel.add(scrollPane, BorderLayout.CENTER);
		
		
		//mainCatPanel.add(panel, BorderLayout.NORTH);
		List<String> mainCheck=mainCatPanel.getMainList();
		int i=0;
		JCheckBox[] checkBoxes = new JCheckBox[mainCheck.size()];
		for(String str:mainCheck)
		{	//String checkstr="checkb"+i;
			checkBoxes[i] = new JCheckBox(str);
			panel.add(checkBoxes[i]);
			panel.repaint();
			panel.revalidate();
			cbox.add(checkBoxes[i]);
			i++;
			//break;
		}
		
		selectMain=new ArrayList<String>();
		for(JCheckBox item:cbox)
		{
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) 
				{
					if(item.isSelected())
					{
						String s=item.getText();
						selectMain.add(s);
						try {
							List<String> sublist=subCatPanel.getSubCat(selectMain);
							populateSubCat(sublist);
							updateLocation(dropdownPanel.getLocations(selectMain));
							updateDay(dropdownPanel.getDay(selectMain));
							updateFrom(dropdownPanel.getFrom(selectMain));
							updateTo(dropdownPanel.getTo(selectMain));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(!item.isSelected())
					{
						String s=item.getText();
						selectMain.remove(s);
						try {
							List<String> sublist=subCatPanel.getSubCat(selectMain);
							populateSubCat(sublist);
							updateLocation(dropdownPanel.getLocations(selectMain));
							updateDay(dropdownPanel.getDay(selectMain));
							updateFrom(dropdownPanel.getFrom(selectMain));
							updateTo(dropdownPanel.getTo(selectMain));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

		}
		
	
		
		
		
		subCatPanel = new SubCatPanel();
		subCatPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Sub-category(optional)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		filterPanel.add(subCatPanel);
		subCatPanel.setLayout(new BorderLayout(0, 0));
		panel_1 = new JPanel();
		panel_1.setLayout(new BoxLayout(panel_1,BoxLayout.Y_AXIS));
		scrollPane_1 = new JScrollPane(panel_1);
		subCatPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		attributePanel = new AttributePanel();
		attributePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Attributes(Optional)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		filterPanel.add(attributePanel);
		attributePanel.setLayout(new BorderLayout(0, 0));
		panel_2 = new JPanel();
		panel_2.setLayout(new BoxLayout(panel_2,BoxLayout.Y_AXIS));
		scrollPane_2 = new JScrollPane(panel_2);
		attributePanel.add(scrollPane_2, BorderLayout.CENTER);
		
		resultPanel = new ResultPanel();
		//tablePane=resultPanel.getScrollPane();
		panel_6.add(resultPanel);
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Desired Businesses", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		resultPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		resultPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.X_AXIS));
		panel_3 = new JPanel();
		panel_3.setLayout(new BoxLayout(panel_3,BoxLayout.Y_AXIS));
		scrollPane_3 = new JScrollPane(panel_3);
		resultPanel.add(scrollPane_3, BorderLayout.CENTER);
		table= new JTable();
		panel_3.add(table);
		
		searchFor = new JPanel();
		searchFor.setBackground(new Color(255, 192, 203));
		searchFor.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchFor.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mainPane.add(searchFor, BorderLayout.NORTH);
		
		lblPleaseSetThe = new JLabel("Please set the filter preference before you proceed!                ");
		lblPleaseSetThe.setFont(new Font("Cambria", Font.BOLD, 18));
		searchFor.add(lblPleaseSetThe);
		String a[]={"DEFAULT","ALL","ANY"};
		decideCB = new JComboBox(a);
		decideCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				String str=(String) decideCB.getSelectedItem();
				if(str.equals("ALL"))
					decide="ALL";
				else
					decide="ANY";
				Component[] comps = panel.getComponents();
				
				for(Component comp: comps)
				{
					if(comp instanceof JCheckBox)
					{
						panel.remove(comp);
						panel.repaint();
						panel.revalidate();
					}
				}
				int i=0;
				JCheckBox[] checkBoxes = new JCheckBox[mainCheck.size()];
				for(String str1:mainCheck)
				{	//String checkstr="checkb"+i;
					checkBoxes[i] = new JCheckBox(str1);
					panel.add(checkBoxes[i]);
					panel.repaint();
					panel.revalidate();
					cbox.add(checkBoxes[i]);
					i++;
					//break;
				}
				
				try {
					populateSubCat(new ArrayList<String>());
					populateAtribute(new ArrayList<String>());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateLocation(new ArrayList<String>());
				updateDay(new ArrayList<String>());
				updateFrom(new ArrayList<String>());
				updateTo(new ArrayList<String>());
				selectMain=new ArrayList<String>();
				for(JCheckBox item:cbox)
				{
					item.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) 
						{
							if(item.isSelected())
							{
								String s=item.getText();
								selectMain.add(s);
								try {
									List<String> sublist=subCatPanel.getSubCat(selectMain);
									populateSubCat(sublist);
									updateLocation(dropdownPanel.getLocations(selectMain));
									updateDay(dropdownPanel.getDay(selectMain));
									updateFrom(dropdownPanel.getFrom(selectMain));
									updateTo(dropdownPanel.getTo(selectMain));
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else if(!item.isSelected())
							{
								String s=item.getText();
								selectMain.remove(s);
								try {
									List<String> sublist=subCatPanel.getSubCat(selectMain);
									populateSubCat(sublist);
									updateLocation(dropdownPanel.getLocations(selectMain));
									updateDay(dropdownPanel.getDay(selectMain));
									updateFrom(dropdownPanel.getFrom(selectMain));
									updateTo(dropdownPanel.getTo(selectMain));
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});

				}
				
			
			}
		});
		

	
		
		decideCB.setBackground(new Color(255, 192, 203));
		searchFor.add(decideCB);
		
		
		
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{	if(selectMain.isEmpty())
				JOptionPane.showMessageDialog(resultPanel, "Please select at least one Main Category!!");
				
				panel_3.remove(table);
				panel_3.repaint();
				panel_3.revalidate();
				
				try {
					table=new JTable(searchPanel.constructSearchQ());
					table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
					table.setBackground(new Color(204,255,229));
					//JOptionPane.showMessageDialog(null, new JScrollPane(table));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				panel_3.add(table);
				panel_3.revalidate();
				panel_3.repaint();
				scrollPane_3.setViewportView(table);
				scrollPane_3.revalidate();
				scrollPane_3.repaint();
				table.addMouseListener(new MouseAdapter() {
					  public void mouseClicked(MouseEvent e) {

					        int row = table.getSelectedRow();
					        int col = table.getSelectedColumn();

					        //build your address / link

					        
					        System.out.println("this is row:"+row+" and this is column:"+col+" that you clicked");
					        String s=(String) table.getValueAt(row,0);
					        try {
								resultPanel.popReviews(s);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					      }
					    });
			} 
			
		
		});
		
		
	}
	public void populateSubCat(List<String> s) throws SQLException
	{
		int i=0;
		
		for(String a:s)
		{
			//System.out.print(a + "_");
		}
		
		Component[] comps = panel_1.getComponents();
		
		for(Component comp: comps)
		{
			if(comp instanceof JCheckBox)
			{
				panel_1.remove(comp);
				panel_1.repaint();
				panel_1.revalidate();
			}
		}
		
		
		subCbox=new ArrayList<JCheckBox>();
		
		JCheckBox[] cb = new JCheckBox[s.size()];
		
		for(String str:s)
		{	//String checkstr="checkb"+i;
			cb[i] = new JCheckBox(str);
			panel_1.add(cb[i]);
			
			subCbox.add(cb[i]);
			i++;
			//break;
		}
		panel_1.revalidate();
		panel_1.repaint();
		
		scrollPane_1.revalidate();
		scrollPane_1.repaint();
		if(selectMain.size()==0)
			populateAtribute(new ArrayList<String>());
		selectSub=new ArrayList<String>();
		for(JCheckBox item:subCbox)
		{
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) 
				{
					if(item.isSelected())
					{
						String s=item.getText();
						selectSub.add(s);
						System.out.println("entered in subcat listener");
						
						try {
							System.out.println(selectMain.size()+" and "+selectSub.size());
							List<String> attriList=attributePanel.getAttribute(selectMain,selectSub);
							populateAtribute(attriList);
							updateLocation(dropdownPanel.getLocations(selectMain,selectSub));
							updateDay(dropdownPanel.getDay(selectMain,selectSub));
							updateFrom(dropdownPanel.getFrom(selectMain,selectSub));
							updateTo(dropdownPanel.getTo(selectMain,selectSub));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else if(!item.isSelected())
					{
						String s=item.getText();
						selectSub.remove(s);
						
						try {
							List<String> attriList = attributePanel.getAttribute(selectMain,selectSub);
							populateAtribute(attriList);
							updateLocation(dropdownPanel.getLocations(selectMain,selectSub));
							updateDay(dropdownPanel.getDay(selectMain,selectSub));
							updateFrom(dropdownPanel.getFrom(selectMain,selectSub));
							updateTo(dropdownPanel.getTo(selectMain,selectSub));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			});

		
		}
		
		
		
	}
	public void populateAtribute(List<String> s)
	{
		int i=0;
		
		for(String a:s)
		{
			//System.out.print(a + "_");
		}
		
		Component[] comps = panel_2.getComponents();
		
		for(Component comp: comps)
		{
			if(comp instanceof JCheckBox)
			{
				panel_2.remove(comp);
				panel_2.revalidate();
				panel_2.repaint();
				
			}
		}
		
		JCheckBox[] att = new JCheckBox[s.size()];
		
		for(String str:s)
		{	//String checkstr="checkb"+i;
			att[i] = new JCheckBox(str);
			panel_2.add(att[i]);
			
			attriBox.add(att[i]);
			System.out.println("adding attribute checkbox");
			i++;
			//break;
			panel_2.revalidate();
			panel_2.repaint();
			
			scrollPane_2.revalidate();
			scrollPane_2.repaint();
		}
		panel_2.revalidate();
		panel_2.repaint();
		
		scrollPane_2.revalidate();
		scrollPane_2.repaint();
		selectAttri=new ArrayList<String>();
		for(JCheckBox item:attriBox)
		{
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) 
				{
					if(item.isSelected())
					{
						String s=item.getText();
						selectAttri.add(s);
						System.out.println("entered in attribute listener");
						try {
							updateLocation(dropdownPanel.getLocations(selectMain,selectSub,selectAttri));
							updateDay(dropdownPanel.getDay(selectMain,selectSub,selectAttri));
							updateFrom(dropdownPanel.getFrom(selectMain,selectSub,selectAttri));
							updateTo(dropdownPanel.getTo(selectMain,selectSub,selectAttri));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					else if(!item.isSelected())
					{
						String s=item.getText();
						selectAttri.remove(s);
						try {
							updateLocation(dropdownPanel.getLocations(selectMain,selectSub,selectAttri));
							updateDay(dropdownPanel.getDay(selectMain,selectSub,selectAttri));
							updateFrom(dropdownPanel.getFrom(selectMain,selectSub,selectAttri));
							updateTo(dropdownPanel.getTo(selectMain,selectSub,selectAttri));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			});

		
		}
		
	}
	public void updateLocation(List<String> locations)
	
	{	dropdownPanel.remove(location);
		
		String locArray[]=new String[locations.size()+1];
		int i=1;
		selectLoc="-SELECT-";
		if(!locations.isEmpty())
			locArray[0]="-SELECT-";
		for(String s:locations)
		{
			locArray[i]=s;
			i++;
		}
		location=new JComboBox(locArray);
		dropdownPanel.add(location,4);
		
		dropdownPanel.revalidate();
		dropdownPanel.repaint();
		location.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				selectLoc= String.valueOf(location.getSelectedItem());	
				System.out.println("entered in location listener");
			}
		});
	}
public void updateDay(List<String> days)
	
	{	dropdownPanel.remove(dayCB);
		String dayArray[]=new String[days.size()+1];
		int i=1;
		selectDay="-SELECT-";
		if(!days.isEmpty())
			dayArray[0]="-SELECT-";
		for(String s:days)
		{
			dayArray[i]=s;
			i++;
		}
		dayCB=new JComboBox(dayArray);
		dropdownPanel.add(dayCB,5);
		
		dropdownPanel.revalidate();
		dropdownPanel.repaint();
		dayCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				selectDay= String.valueOf(dayCB.getSelectedItem());		
				System.out.println("entered in day listener");
			}
		});
	}
public void updateFrom(List<String> from)

{	dropdownPanel.remove(fromCB);
	String fromArray[]=new String[from.size()+1];
	int i=1;
	selectFrom="-SELECT-";
	if(!from.isEmpty())
		fromArray[0]="-SELECT-";
	for(String s:from)
	{
		fromArray[i]=s;
		i++;
	}
	fromCB=new JComboBox(fromArray);
	dropdownPanel.add(fromCB,6);
	
	dropdownPanel.revalidate();
	dropdownPanel.repaint();
	fromCB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ev) 
		{
			selectFrom= String.valueOf(fromCB.getSelectedItem());
			System.out.println("entered in from listener");
		}
	});
	
}
public void updateTo(List<String> to)

{	dropdownPanel.remove(toCB);
	String toArray[]=new String[to.size()+1];
	int i=1;
	selectTo="-SELECT-";
	if(!to.isEmpty())
		toArray[0]="-SELECT-";
	for(String s:to)
	{
		toArray[i]=s;
		i++;
	}
	toCB=new JComboBox(toArray);
	dropdownPanel.add(toCB,7);
	
	dropdownPanel.revalidate();
	dropdownPanel.repaint();
	toCB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ev) 
		{
			selectTo= String.valueOf(toCB.getSelectedItem());		
			System.out.println("entered in to listener");
		}
	});
	
}
public static void popPreference(JFrame frame)
{	
	
	Object[] options = {"Yes, please",
    "No way!"};
int n = JOptionPane.showOptionDialog(frame,
"Would you like green eggs and ham?",
"A Silly Question",
JOptionPane.YES_NO_OPTION,
JOptionPane.QUESTION_MESSAGE,
null,     //do not use a custom Icon
options,  //the titles of buttons
options[0]); //default button title
}
}

