package API.model;

public class TransitionModel {
	private String idTransitionRollBack;
	private String idTransitionResolvedByAllS;
	public String getIdTransitionRollBack() {
		return idTransitionRollBack;
	}
	public void setIdTransitionRollBack(String idTransitionRollBack) {
		this.idTransitionRollBack = idTransitionRollBack;
	}
	public String getIdTransitionResolvedByAllS() {
		return idTransitionResolvedByAllS;
	}
	public void setIdTransitionResolvedByAllS(String idTransitionResolvedByAllS) {
		this.idTransitionResolvedByAllS = idTransitionResolvedByAllS;
	}
	public TransitionModel(String idTransitionRollBack, String idTransitionResolvedByAllS) {
		super();
		this.idTransitionRollBack = idTransitionRollBack;
		this.idTransitionResolvedByAllS = idTransitionResolvedByAllS;
	}
}
