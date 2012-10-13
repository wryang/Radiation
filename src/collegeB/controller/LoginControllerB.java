package collegeB.controller;

import java.sql.SQLException;

import collegeA.controller.AdministerViewControllerA;
import collegeB.server.ServerB;
import collegeB.view.AdministerViewB;
import collegeB.view.LoginViewB;
import collegeB.view.StudentViewB;

public class LoginControllerB {
	private LoginViewB view = new LoginViewB();
	private ServerB model;
	
	public LoginControllerB(){
		this.model = ServerB.getInstance();
	}
	
	public void setView(LoginViewB view){
		this.view = view;
	}
	
	public void loginButtonClicked(){
		String username = view.getUsername();
		String password = view.getPassword();

		if(model.validate(username, password)){
			if(model.isAdminister()){
				//admin
				view.close();
				AdministerViewB adminView = new AdministerViewB();
				AdministerViewControllerB adminController = new AdministerViewControllerB();
				adminController.setView(adminView);
				adminView.setController(adminController);
				adminView.show();
			}
			else{
				view.close();
				StudentViewB stdView = new StudentViewB();
	//			StudentViewControllerA stdController = new StudentViewControllerA(stdView);
			}
		}else
			view.showErrorMessage("用户名或密码错误!");
	}
}

