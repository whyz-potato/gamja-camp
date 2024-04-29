package whyzpotato.gamjacamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whyzpotato.gamjacamp.domain.chat.Chat;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByPost_Id(Long postId);
}
