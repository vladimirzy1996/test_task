package by.vote.test.app.controllers;


import java.util.Collection;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import by.vote.test.app.entity.Vote;
import by.vote.test.app.entity.VotingOption;

@RestController
public class VotingOptionController {
	private Vote votesCheck(Integer id) {
		Map<Integer, Vote> voteMap = VoteController.getVoteMap();
		if (voteMap == null) {
			return null;
		}
		Vote vote = voteMap.get(id);
		if (vote == null) {
			return null;
		}
		return vote;
	}

	private void save(Integer id, Map<Integer, VotingOption> votingOptionMap) {
		Map<Integer, Vote> voteMap = VoteController.getVoteMap();
		Vote vote = voteMap.get(id);
		vote.setVotingOptions(votingOptionMap);
		voteMap.remove(id);
		voteMap.put(id, vote);
		VoteController.setVoteMap(voteMap);
	}

	@RequestMapping(value = "/api/votes/{id}/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<VotingOption>> getOptions(@PathVariable("id") Integer id) {
		Vote vote = votesCheck(id);
		if (vote == null) {
			new ResponseEntity<Collection<VotingOption>>(HttpStatus.NOT_FOUND);
		}
		Map<Integer, VotingOption> votingOptions = vote.getVotingOptions();
		if (votingOptions.isEmpty()) {
			return new ResponseEntity<Collection<VotingOption>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Collection<VotingOption>>(votingOptions.values(), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/votes/{id}/options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VotingOption> createOption(@PathVariable("id") Integer id,
			@RequestBody VotingOption votingOption) {
		Vote vote = votesCheck(id);
		if (vote != null) {
			if (votingOption != null) {
				if (votingOption.getId() == null) {
					Map<Integer, VotingOption> votingOptions = vote.getVotingOptions();
					votingOption.setId(votingOptions.size()+1);
					votingOptions.put(votingOption.getId(), votingOption);
					save(id, votingOptions);
					return new ResponseEntity<VotingOption>(votingOption, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<VotingOption>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/votes/{id}/options/{optionId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VotingOption> updateOption(@PathVariable("id") Integer id,
			@PathVariable("optionId") Integer optionId, @RequestBody VotingOption votingOption) {
		Vote vote = votesCheck(id);
		if (vote == null) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		Map<Integer, VotingOption> votingOptions = vote.getVotingOptions();
		if (votingOptions == null || votingOptions.isEmpty()) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		VotingOption oldVotingOption = votingOptions.get(optionId);
		if (oldVotingOption == null) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		if(!optionId.equals(votingOption.getId())){
			return new ResponseEntity<VotingOption>(HttpStatus.BAD_REQUEST);
		}
		votingOptions.remove(optionId);
		votingOptions.put(optionId, votingOption);
		save(id, votingOptions);
		return new ResponseEntity<VotingOption>(votingOption, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/api/votes/{id}/options/{optionId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<VotingOption> deleteOption(@PathVariable("id") Integer id,
			@PathVariable("optionId") Integer optionId) {
		Vote vote = votesCheck(id);
		if (vote == null) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		Map<Integer, VotingOption> votingOptions = vote.getVotingOptions();
		if (votingOptions == null || votingOptions.isEmpty()) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		VotingOption oldVotingOption = votingOptions.get(optionId);
		if (oldVotingOption == null) {
			return new ResponseEntity<VotingOption>(HttpStatus.NOT_FOUND);
		}
		votingOptions.remove(optionId);
		save(id,votingOptions);
		return new ResponseEntity<VotingOption>(HttpStatus.NO_CONTENT);
	}

}
