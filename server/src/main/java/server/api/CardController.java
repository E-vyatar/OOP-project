/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.BoardRepository;

import java.util.Random;

@RestController
@RequestMapping("/api/cards") // temporary!!!!!!!
public class CardController {

    private final Random random;
    private final BoardRepository repo;

    public CardController(Random random, BoardRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    // wait for board\list repo
//    @GetMapping(path = {"", "/"})
//    public List<Card> getAll() {
//        return repo.findAll();
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
//        if (id < 0 || !repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(repo.findById(id).get());
//    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Card> add(@RequestBody Card card) {

        if (isNullOrEmpty(card.getCardTitle())) {
            return ResponseEntity.badRequest().build();
        }

//        Card saved = repo.save(card);
//        return ResponseEntity.ok(saved);
        return null;
    }
}