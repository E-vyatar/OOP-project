# Code Contributions and Code Reviews

#### Focused Commits

* Grade: Very Good

* Feedback: 
  * Top: I see that you are more consistent with your messages, and that you already started to implement some common naming conventions
  * Top: Commits focused and add only add one functionality at a time.
  * Top: I also see that some people added descriptions to thier commit messages; while this is great, if you think that you spend too much time on these,
  you can also opt not to write them, especially when it can be summarized in the title.
  * Tip: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/commit/50a25780a302320077fd0bf50691d364bfccf74f is an example of a very big
  commit (800 lines of code added), with the title "fast forward". This is not descriptive and adds a lot of functionality at a time, which is not ideal.

#### Isolation

* Grade: Very Good

* Feedback: 
  * Top: All features are developed in separate branches which are merged to main via a MR.
  * Top: It is great that you are consistent with the names (issue number-description). Make sure that you keep consistency!


#### Reviewability

* Grade: Sufficient

* Feedback: 
  * Tip: If your commit messages are starting to get better and more descriptive, make sure that is the case for MRs as well.]
  * Tip: I also see that most MRs do not have a description. This one: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/merge_requests/36
  , which has description: I added all the API stuff. I also added a property to commons.CardList because it was missing 'boardId', which was crucial in some of the get requests.
  Which all API stuff? The description could be a bit more formal as well. On the other hand, this MR: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/merge_requests/46
  has almost no decription: if you say it does not close the issue completely, what does it add exaclt, and what still needs to be added?


#### Code Reviews

* Grade: Insufficient

* Feedback: 
  * Tip: Make sure that when you create an MR you assign at least 2 people to review your code (it means that they will look over it and actually check if it is working; as a reviewer, you can comment about code style/names of variables/ anything that is unclear or doesn’t make sense/ ask questions about the code etc). From my experience, there are almost no MR that are perfect from the beginning, so you will have things to give feedback on.
  * Tip: again with this MR: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/merge_requests/46 . There are no reviewer assigned to it, also no assignee,
  no estimated time that took to complete the task and no comments. Moreover, there are no reviews from the other teammates and the approval is from the same
  person that crated the MR. Please, don't do that again, even when you really really want your MR approved:)
  * Tip: same for this: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/merge_requests/38 . Approved by the person that created the MR. I like that 
there are some reviews, but for a MR that adds 800 lines of code it might need more, from different people.

#### Build Server

* Grade: Sufficient
* Feedback: 
    * I see that the checkstyle file is still empty on main and dev: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-17/-/blob/main/checkstyle.xml
If you don't add it soon, you will have to fix a lot of issues when adding it later, as the pipeline won't pass anymore.
