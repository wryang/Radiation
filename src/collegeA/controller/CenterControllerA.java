package collegeA.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;

import collegeA.view.LoginViewA;

public class CenterControllerA {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new RavenSkin());
			}
		});
		
		initializeLoginView();
	}
	
	private static void initializeLoginView(){
		LoginViewA loginView = new LoginViewA();
		LoginControllerA loginController = new LoginControllerA();
		loginView.setController(loginController);
		loginController.setView(loginView);
		loginView.show();
	}
}
