package collegeB.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import collegeB.controller.LoginControllerB;
import collegeB.util.SpringUtilities;

public class LoginViewB {
	
	private JFrame frame;
	private LoginControllerB controller;
	//private final Properties langConfig;
	private JTextField usernameFld;
	private JPasswordField passwordFld;
	private JLabel messageLbl;
	private JButton loginButton;
	private JMenuItem settingItem;
	private JMenuItem forgetPassItem;

	public LoginViewB(){
		//langConfig = Configurator.getLanguageConfig();
		initializeComponents();
		addListeners();
	}
	
	private void initializeComponents(){
		frame = new JFrame("B 学院 - 登录");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel avatarLbl =  new JLabel();
		JLabel usernameLbl = new JLabel("用户名:");
		usernameFld = new JTextField(10);
		JLabel passwordLbl = new JLabel("密码:");
		passwordFld = new JPasswordField(10);
		
		messageLbl = new JLabel(" ");
		messageLbl.setForeground(Color.RED);
		
		loginButton = new JButton("登录");
		loginButton.setToolTipText("点击登录");
		
		JPanel springPanel = new JPanel(new SpringLayout());
		springPanel.add(usernameLbl);
		springPanel.add(usernameFld);
		springPanel.add(passwordLbl);
		springPanel.add(passwordFld);
		SpringUtilities.makeCompactGrid(springPanel, 2, 2, 6, 6, 6, 6);
		
		JPanel springWrapper = new JPanel();
		springWrapper.add(springPanel);
		
		JPanel flowWrapper = new JPanel();
		flowWrapper.add(loginButton);
		
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		boxPanel.add(springWrapper);
		boxPanel.add(messageLbl);
		boxPanel.add(flowWrapper);
		
		frame.getContentPane().add(avatarLbl, BorderLayout.NORTH);
		frame.getContentPane().add(boxPanel, BorderLayout.CENTER);
//		frame.pack();
	}
	
	private void addListeners(){
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.loginButtonClicked();
			}
		});
		
		KeyListener keyListener = new KeyListener(){
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER)
					controller.loginButtonClicked();
			}

			@Override
			public void keyReleased(KeyEvent event) {	
			}

			@Override
			public void keyTyped(KeyEvent event) {
			}
		};
		
		usernameFld.addKeyListener(keyListener);
		passwordFld.addKeyListener(keyListener);
	}

	public void finalize() throws Throwable {

	}
	
	public String getPassword(){
		return passwordFld.getText();
	}

	public String getUsername(){
		return usernameFld.getText();
	}

	public boolean rememberPassword(){
		return false;
	}

	/**
	 * 
	 * @param controller
	 */
	public void setController(LoginControllerB controller){
		this.controller = controller;
	}

	/**
	 * 
	 * @param message
	 */
	public void showErrorMessage(String message){
		messageLbl.setText(message);
	}

	
	public void close(){
		frame.dispose();
	}
	
	public void show(){		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(300, 200);
		Dimension frameSize = frame.getSize();
		frame.setLocation(screenSize.width/2 - frameSize.width/2, screenSize.height/2 - frameSize.height/2);	
		frame.setVisible(true);
	}
}

