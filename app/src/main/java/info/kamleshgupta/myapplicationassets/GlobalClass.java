package info.kamleshgupta.myapplicationassets;

import android.app.Application;


public class GlobalClass extends Application{

	private int OrdNo;
	private String ShortComp;
	private String email;
	private String BCode;
	private String Heading;
	private String BType;
	private String Status;
	private String RefAction;
	private String DashHeading;
	private String RefActiond;
	private String UType;

	public int getOrdNO() {

		return OrdNo;
	}

	public void setOrdNo(int Ord) {
		OrdNo = Ord;
	}

	public String getShortComp() {

		return ShortComp;
	}

	public void setShortComp(String aSCName) {

		ShortComp = aSCName;

	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String aEmail) {

		email = aEmail;
	}
	public String getBranch() {

		return BCode;
	}

	public void setBranch(String BCode) {

		this.BCode = BCode;
	}

	public String getHeading() {

		return Heading;
	}

	public void setHeading(String Heading) {

		this.Heading = Heading;
	}

	public String getBType() {

		return BType;
	}

	public void setBType(String BType) {

		this.BType = BType;
	}

	public String getStatus() {

		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public String getRefAction() {

		return RefAction;
	}

	public void setRefAction(String RefAction) {
		this.RefAction = RefAction;
	}

	public String getRefActiond() {

		return RefActiond;
	}

	public void setRefActiond(String RefActiond) {
		this.RefActiond = RefActiond;
	}

	public String getDashHeading() {

		return DashHeading;
	}

	public void setDashHeading(String DashHeading) {
		this.DashHeading = DashHeading;
	}


	public String getUType() {

		return UType;
	}

	public void setUType(String UType) {

		this.UType = UType;
	}
}