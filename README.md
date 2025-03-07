# Chatty - Simple Chat Bot
## Intro

We're creating virtual assistants. They can help people with a lot of topics
or solve numerous problems. The assistant is powered by AI with possibility to lead conversation and react on client's wishes.

The Simple Chat Bot is emulating the assistant behavior with a simple logic.
Let's call him Chatty. He should be kind and should know to greet. There are two areas what Chatty knows

- tell a joke,
- convert amount of money to specified currency.

## Goal is to write Chatty's backend

As there's no AI behind this application, let's stick in the simple string matching and use possibly regular expressions for parsing client's questions. The questions are predefined and whenever Chatty doesn't understand, he should simply respond that to the client. It's not requested to have over-killed solution with handling all possible question variants. Keep it simple but don't forget to handle error cases.



There's no state handled in the application or towards the client. Chatty simply responses what's he asked for. When a client asks for the same question twice, Chatty answers not depending on previous answer.

### Jokes

To have a fun with Chatty, a client can ask him to tell a joke. Let's have a set of predefined jokes in the code and randomly select one of them. It's enough to have only a few of them, and you can take them from your memory or just simply search for them over internet.

### Currency Conversion

There should be integrated currency convertor in Chatty. He should be able to answer how much money is in another specified currency.
For simplicity, we can stick in the currency codes only. The conversion should be done inside the app, just the conversion rates are downloaded from some public API.

In the app, there should be a client downloading current currency rates from a public API. It's up to you if rates are downloaded for each of the request or cached or regularly downloaded but keep it simple and assume that we don't need to have any data storage (and just use in-memory structures). By aware that there can be any throttling in a public API, and we should not overheat it.
Consuming so much memory of the app is also not the best approach. If you chose any caching or store data in memory, assume that the base currency (eg. EUR) is used for conversion between two other currencies (eg. converting between USD and AUD is done over EUR because we don't have the exact conversion rate directly from USD).

We're not requesting to use any specific public API but this API should
be available without any registration or pricing plan. You can use
eg. [Frankfurter](https://www.frankfurter.app/) or some central banks
has also readable format, eg. [Czech National Bank](https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt).



The app should be ready for the state when consumed API is not available or data are not valid - let's assume that data are valid for one whole day.
There should be also available answer, when client is asking for non-existing currency. Don't forget to use proper HTTP codes in case of there's a failure.


## Non-functional Requirements



Assume that the code is deployed in a production environment and depending on that, cover it with tests and focus on final code quality. Think about possible extensions of the application (for example, other knowledge areas). Do not make a solution which is over-engineered or over-killed. Simplicity makes it easier to understand. When you develop, think about others who should touch your code. Wherever you might be sure there's documentation needed, make it there. The history of changes of your final solution could also help other developers in future to understand your ideas.


Choose one of your preferred JVM language:

- Java (min 11)

- Kotlin (min 1.5)

We're using SpringBoot framework but if you prefer any other framework, feel free to use it.

Use Maven or Gradle for building the project. Keep in mind that there must not be any obstacle with building or running the application depending on usage of any kind of build platform. The project should be running when the repository is locally cloned.

Using so many libraries or any kind of magic for this simple application is also not a preferred solution.

Upload your solution to [Github](https://github.com) where you create private repository
for the project. When you're ready with the solution, add access to this repository for

dominik.plisek@addai.cz

The goal is to have working PoC showing how you develop, how you tackle this challenge.

You should be happy with that solution but if you struggle at some point, finish what you can in your best.

## Endpoints


### GET /questions

This is endpoint listing possible questions which Chatty could accept. We're not limiting this list, and it's only up to you how creative you are.



#### Response

The list of string is returned. The placeholders are there instead of specific amounts or currencies which client can
ask for.
Here, we don't want to expose any regular expression or internal parsing logic.

```
{

"questions": []string
}

```

#### Example

> GET /questions

```json

{

"questions": [

"Can you tell me a joke?",

"Tell me a joke",

"Convert <amount> <from> to <to>",

"How much is <amount> <from> in <to>"

]

}

```

### POST /conversation

In this endpoint, Chatty reacts on client's question. Don't forget that he should be kind and know to respond on client's
greeting.

Frankfurter | Free exchange rates and currency data API
Frankfurter is a free, open-source API for exchange rates. Access current and historical currency data, and more.