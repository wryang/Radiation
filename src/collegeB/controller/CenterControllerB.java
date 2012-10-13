package collegeB.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;

import collegeB.view.LoginViewB;

public class CenterControllerB {

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
		LoginViewB loginView = new LoginViewB();
		LoginControllerB loginController = new LoginControllerB();
		loginView.setController(loginController);
		loginController.setView(loginView);
		loginView.show();
	}
}
