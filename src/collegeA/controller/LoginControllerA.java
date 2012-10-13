package collegeA.controller;

import java.sql.SQLException;

import collegeA.server.ServerA;
import collegeA.view.AdministerViewA;
import collegeA.view.LoginViewA;
import collegeA.view.StudentViewA;

public class LoginControllerA {
	private LoginViewA view = new LoginViewA();
	private ServerA model;
	
	public LoginControllerA(){
		ServerA model = ServerA.getInstance();
		this.model = model;
	}
	
	public void setView(LoginViewA view){
		this.view = view;
	}
	
	public void loginButtonClicked() throws SQLException{
		String username = view.getUsername();
		String password = view.getPassword();

		if(model.validate(username, password)){
			if(model.isAdminister()){
				//admin
				view.close();
				AdministerViewA adminView = new AdministerViewA();
				AdministerViewControllerA adminController = new AdministerViewControllerA();
				adminController.setView(adminView);
				adminView.setController(adminController);
				adminView.show();
			}
			else{
				view.close();
				StudentViewA stdView = new StudentViewA();
	//			StudentViewControllerA stdController = new StudentViewControllerA(stdView);
			}
		}else
			view.showErrorMessage("用户名或密码错误!");
	}
}
