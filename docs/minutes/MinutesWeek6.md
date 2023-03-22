# Meeting Minutes Week 6 [45min]
Date:               March 21st 2023 @ 13:45\
Main focus:     Project heuristics and overall evaluation of what we have so far\
Chair:              Rares Popa\
Note taker:      Evyatar Hadasi

## Opening [5-10min]

### Opening by Chair [1min]
*Is everyone present?*\
Everyone is present, Romir joined online.

### Check-in [1-5min]
*How is everyone doing?*\
Everyone is good.

### Approval of the agenda [2-3min]
*Did everyone see the agenda schedule for today? Any suggestions ?*\
Approved.

### Approval of last-week minutes [2-3min]
*Did everyone read the notes from the previous meeting? Are there any objections or things I've missed?*\
Approved.

## Today's Topics [30-40min]

### TA Feedback [5-10min]
*Did everyone read the TA feedback? We will discuss any inconvenience about the feedback*\
Everyone read.
- Biggest issue is we're not reviewing each other's work
- Everything code discussion should be on git, not whatsapp
- When creating a merge request we should assign reviewers, preferably related people but if no one is related then assign according to personal feeling
- Approval of merge request should be after all threads are resolved
- Checkstyle file is still empty. We should make it work soon to avoid failed build on all the code we already did
- We can use the OOP course checkstyle file
- Romir will run checkstyle on dev branch and fix warnings
- We don't have to use mocking for tests, postman is enough (Romir will add postman repository for reference)
- Everyone should have about 2-3 merge requests each week so their contribution to code can be seen, also should have new lines of code written to be shown in weekly statistics
- If your merge request contains code from someone else block your merge request by theirs so their code will be counted as theirs and not yours

### Brief talk about the functionality and our product so far [10-15min]
*Here we will discuss how is everyone currently handling their tasks and have a brief conversation about how everything is going so far*
*Also we will discuss the stage of all the issues and which will be closed and which won't*
Application has a lot of hard-coded data, we need to delete and replace it with actual server communication.\
What everyone did so far:
- Rares started the UI for server connect. He also did web sockets, but it's not working yet
- Abi fixed card UI problems and moved board related code to the board controller
- Finn added server communication for new list and is making new UI design
- Kai almost finished the API. He also started working on the rename list pop-up functionality
- Evy made cards features: add, edit and delete, and he is almost done
- Romir worked on the database (switched back from mongo to h2), and the drag and drop feature

We showed Alexandra the UI we have and gave some explanations on what works right now. 

### Heuristic Evaluation [5-10min]
*Talk about how the Heuristic Evaluation will take place and what team we will do it together with*\
Abi was contacted but nothing was decided yet with the other group. We're planning to send mock-ups because the application is not functional yet.
The draft should contain as much as possible so Alexandra has a good idea about how we're doing.
When doing the individual evaluations we need to make each comment match at least one heuristic. 

## End of Meeting [~10min]

### Next meeting [5min]
*What should be goal and points of the next meeting and a general agreement on what we are going to do?*\
A goal was set to finish Basic requirements until next week.

## Question Round [5min]
*If there are any questions now is the time to ask them.*
