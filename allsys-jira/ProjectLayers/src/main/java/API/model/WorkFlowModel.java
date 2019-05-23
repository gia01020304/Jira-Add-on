package API.model;

import java.util.ArrayList;
import java.util.List;

public class WorkFlowModel {
	private List<Integer>listIDTransition;

	public List<Integer> getListIDTransition() {
		return listIDTransition;
	}

	public void setListIDTransition(List<Integer> listIDTransition) {
		this.listIDTransition = listIDTransition;
	}

	public WorkFlowModel() {
		super();
		this.listIDTransition = new ArrayList<>();
	}
}
