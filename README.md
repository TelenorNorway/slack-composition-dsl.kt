# Slack BlockKit DSL

A simple composition API for Slack.

<!-- @formatter:off -->
```kt
fun dailyQuiz(
  builder: ChatPostMessageRequest.ChatPostMessageRequestBuilder,
  channel: String,
  thread: String?,
) = builder.channel(channel).threadTs(thread).text(" ").blocks {
  +section(markdown("*Daily Quiz*"))
  +section(markdown("When was Slack initially released?"))
  +divider()
  +actions {
    +button("a", "A) 2009")
    +button("b", "B) 2010")
    +button("c", "B) 2013")
    +button("d", "D) 2014")
  }
}
```
<!-- @formatter:on -->
