package collegeC.bean;

public class CourseC {
	private String cno;
	private String cnm;
	private Integer ctm;
	private Integer cpt;
	private String tec;
	private String pla;
	private Boolean share = NON_SHARED;

	public static Boolean SHARED = true;
	public static Boolean NON_SHARED = false;

	public static String INNER_COURSE_ID = "3";
	
	public CourseC() {

	}
	
	public boolean isLocalCourse() {
		if(cno.startsWith(INNER_COURSE_ID)) {
			return true;
		}else {
			return false;
		}
	}

	public CourseC(String cno, String cnm, int ctm, int cpt, String tec,
			String pla, Boolean share) {
		this.cnm = cnm;
		this.cno = cno;
		this.cpt = cpt;
		this.ctm = ctm;
		this.pla = pla;
		this.setShare(share);
		this.tec = tec;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getCnm() {
		return cnm;
	}

	public void setCnm(String cnm) {
		this.cnm = cnm;
	}

	public Integer getCtm() {
		return ctm;
	}

	public void setCtm(Integer ctm) {
		this.ctm = ctm;
	}

	public Integer getCpt() {
		return cpt;
	}

	public void setCpt(Integer cpt) {
		this.cpt = cpt;
	}

	public String getTec() {
		return tec;
	}

	public void setTec(String tec) {
		this.tec = tec;
	}

	public String getPla() {
		return pla;
	}

	public void setPla(String pla) {
		this.pla = pla;
	}

	public Boolean getShare() {
		return share;
	}

	public void setShare(Boolean share) {
		this.share = share;
	}

}
