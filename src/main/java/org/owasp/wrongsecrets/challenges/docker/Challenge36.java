package org.owasp.wrongsecrets.challenges.docker;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.owasp.wrongsecrets.RuntimeEnvironment;
import org.owasp.wrongsecrets.ScoreCard;
import org.owasp.wrongsecrets.challenges.Challenge;
import org.owasp.wrongsecrets.challenges.ChallengeTechnology;
import org.owasp.wrongsecrets.challenges.Difficulty;
import org.owasp.wrongsecrets.challenges.Spoiler;
import org.owasp.wrongsecrets.challenges.docker.binaryexecution.BinaryExecutionHelper;
import org.owasp.wrongsecrets.challenges.docker.binaryexecution.MuslDetectorImpl;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This is a binary based challenge with encryption involved to make it harder to find the answer
 */
@Slf4j
@Component
@Order(36) // make sure this number is the same as your challenge
public class Challenge36 extends Challenge {
  private final BinaryExecutionHelper binaryExecutionHelper;
  private String executable;

  public Challenge36(ScoreCard scoreCard) {
    super(scoreCard);
    this.executable = "wrongsecrets-advanced-c";
    this.binaryExecutionHelper = new BinaryExecutionHelper(36, new MuslDetectorImpl());
  }

  // is this challenge usable in CTF mode?
  @Override
  public boolean canRunInCTFMode() {
    return true;
  }

  // return the plain text secret here
  @Override
  public Spoiler spoiler() {
    return new Spoiler(binaryExecutionHelper.executeCommand("spoil", executable));
  }

  // here you validate if your answer matches the secret
  @Override
  public boolean answerCorrect(String answer) {
    return binaryExecutionHelper
        .executeCommand(answer, executable)
        .equals("This is correct! Congrats!");
  }

  // which runtime can you use to run the challenge on? (You can just use Docker here)
  /** {@inheritDoc} */
  @Override
  public List<RuntimeEnvironment.Environment> supportedRuntimeEnvironments() {
    return List.of(RuntimeEnvironment.Environment.DOCKER);
  }

  // set the difficulty: 1=low, 5=very hard
  /** {@inheritDoc} */
  @Override
  public int difficulty() {
    return Difficulty.EXPERT;
  }

  // on which tech is this challenge? See ChallengeTechnology.Tech for categories
  /** {@inheritDoc} Secrets based. */
  @Override
  public String getTech() {
    return ChallengeTechnology.Tech.BINARY.id;
  }

  @Override
  public boolean isLimitedWhenOnlineHosted() {
    return false;
  }
}
