package ru.bevz.freeter.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bevz.freeter.domain.Message;
import ru.bevz.freeter.domain.User;
import ru.bevz.freeter.domain.dto.MessageDto;

@Repository
public interface MessageRepo extends CrudRepository<Message, Long> {

    @Query(
            """
            select new ru.bevz.LC_SB2.domain.dto.MessageDto(
            m,
            count(ml),
            sum(case when ml = :user then 1 else 0 end) > 0
            )
            from Message m left join m.likes ml
            group by m
            """
    )
    Page<MessageDto> findAll(Pageable pageable, User user);

    @Query(
            """
            select new ru.bevz.LC_SB2.domain.dto.MessageDto(
            m,
            count(ml),
            sum(case when ml = :user then 1 else 0 end) > 0
            )
            from Message m left join m.likes ml
            where m.tag = :tag
            group by m
            """
    )
    Page<MessageDto> findByTag(String tag, Pageable pageable, User user);

    @Query(
            """
            select new ru.bevz.LC_SB2.domain.dto.MessageDto(
            m,
            count(ml),
            sum(case when ml = :user then 1 else 0 end) > 0
            )
            from Message m left join m.likes ml
            where m.author = :author
            group by m
            """
    )
    Page<MessageDto> findByAuthor(Pageable pageable, User author, User user);

}
