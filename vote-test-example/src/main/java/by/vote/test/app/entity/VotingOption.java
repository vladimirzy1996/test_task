package by.vote.test.app.entity;

import java.math.BigInteger;

public class VotingOption {
	private Integer id;
	private String text;
	private Integer voteNumbers;

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

	public Integer getVoteNumbers() {
		return voteNumbers;
	}

	public void setVoteNumbers(Integer voteNumbers) {
		this.voteNumbers = voteNumbers;
	}
}
