package space.hideaway.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.hideaway.repositories.PasswordTokenRepository;
import space.hideaway.repositories.VerificationTokenRepository;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokensPurgeTask {

    @Autowired
    VerificationTokenRepository tokenRepository;
    @Autowired
    PasswordTokenRepository resetRepository;

    @Scheduled(cron = "${purge.cron.expression}")//will run and purge expired tokens every 24 hours
    public void purgeExpired() {

        Date now = Date.from(Instant.now());

        tokenRepository.deleteAllExpiredSince(now);
        resetRepository.deleteAllExpired(now);
    }
}
