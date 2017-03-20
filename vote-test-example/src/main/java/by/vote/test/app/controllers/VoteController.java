package by.vote.test.app.controllers;

import java.util.Collection;
import java.util.HashMap;
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
public class VoteController {
	private static Map<Integer, Vote> voteMap = new HashMap<Integer, Vote>();
	private static Integer nextId =1;

	public static Map<Integer, Vote> getVoteMap() {
		return voteMap;
	}

	private static Vote save(Vote vote) {
		Integer voteId = vote.getId();
		if (voteId != null) {
			Vote oldVote = voteMap.get(voteId);
			if (oldVote == null) {
				return oldVote;
			}
			voteMap.remove(voteId);
			voteMap.put(voteId, vote);
			return vote;
		}
		vote.setId(nextId);
		voteMap.put(vote.getId(), vote);
		nextId = voteMap.size() + 1;
		return vote;

	}

	public static void setVoteMap(Map<Integer, Vote> newVoteMap) {
		voteMap = newVoteMap;
	}

	private static boolean delete(Integer id) {
		Vote deletedVote = voteMap.remove(id);
		if (deletedVote == null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/api/votes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Vote>> getVotes() {
		if (voteMap == null) {
			return new ResponseEntity<Collection<Vote>>(HttpStatus.NO_CONTENT);
		}
		Collection<Vote> votes = voteMap.values();
		return new ResponseEntity<Collection<Vote>>(votes, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/votes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vote> getVote(@PathVariable("id") Integer id) {
		Vote vote = voteMap.get(id);
		if (vote == null) {
			return new ResponseEntity<Vote>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vote>(vote, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/votes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
		if (vote == null) {
			return new ResponseEntity<Vote>(HttpStatus.BAD_REQUEST);
		}
		if(vote.getId()!=null){
			return new ResponseEntity<Vote>(HttpStatus.BAD_REQUEST);
		}
		Vote savedVote = save(vote);
		return new ResponseEntity<Vote>(savedVote, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/api/votes/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vote> updateVote(@PathVariable("id") Integer id, @RequestBody Vote vote) {
		if (voteMap.get(id) == null) {
			return new ResponseEntity<Vote>(HttpStatus.NOT_FOUND);
		}
		if(!id.equals(vote.getId())){
			return new ResponseEntity<Vote>(HttpStatus.BAD_REQUEST);
		}
		Vote updatedVote = save(vote);
		if (updatedVote == null) {
			return new ResponseEntity<Vote>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vote>(updatedVote, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/votes/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vote> deleteVote(@PathVariable("id") Integer id) {
		boolean deleted = delete(id);
		if (!deleted) {
			return new ResponseEntity<Vote>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vote>(HttpStatus.NO_CONTENT);
	}
}
