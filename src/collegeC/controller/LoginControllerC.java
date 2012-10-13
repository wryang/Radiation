package collegeC.controller;

import collegeC.server.ServerC;
import collegeC.view.AdministerViewC;
import collegeC.view.LoginViewC;
import collegeC.view.StudentViewC;

public class LoginControllerC {
	
	/**
	 * @param args
	 */
	private LoginViewC view = new LoginViewC();
	private ServerC model;
	
	public LoginControllerC(){
		this.model = ServerC.getInstance();
	}
	public void setView(LoginViewC view){
		this.view = view;
	}
	
	public void loginButtonClicked(){
		String username = view.getUsername();
		String password = view.getPassword();

		if(model.validate(username, password)){
			if(model.isAdminister()){
				//admin
				view.close();
				AdministerViewC adminView = new AdministerViewC();
				AdministerViewControllerC adminController = new AdministerViewControllerC();
				adminController.setView(adminView);
				adminView.setController(adminController);
				adminView.show();
			}
			else{
				view.close();
				StudentViewC stdView = new StudentViewC();
	//			StudentViewControllerA stdController = new StudentViewControllerA(stdView);
			}
		}else
			view.showErrorMessage("用户名或密码错误!");
	}
}
