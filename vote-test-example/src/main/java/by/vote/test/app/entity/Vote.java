package by.vote.test.app.entity;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Vote {
	private Integer id;
	private String text;
	private Map<Integer,VotingOption> votingOptions;

	public Map<Integer,VotingOption> getVotingOptions() {
		return votingOptions;
	}

	public void setVotingOptions(Map<Integer,VotingOption> votingOptions) {
		this.votingOptions = votingOptions;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
