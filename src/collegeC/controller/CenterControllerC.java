package collegeC.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;

import collegeC.view.LoginViewC;

public class CenterControllerC {
	
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
		LoginViewC loginView = new LoginViewC();
		LoginControllerC loginController = new LoginControllerC();
		loginView.setController(loginController);
		loginController.setView(loginView);
		loginView.show();
	}
}
