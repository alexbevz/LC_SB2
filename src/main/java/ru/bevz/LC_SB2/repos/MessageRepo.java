package ru.bevz.LC_SB2.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bevz.LC_SB2.domain.Message;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
