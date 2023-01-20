# Anonymous_Chat
General information: the application is a telegram bot.

Main features: automatic registration of the user in the database when the bot is first launched. Search for a companion, messaging, stop the dialogue, search for a new companion.

Run: users are stored in a database (SQL). When clicking on "search new chat", the user is recorded in the array for waiting users, and if there are other people besides him, the bot randomly selects a second person and records their connection in the second array, and removes these users from the array of people in search. When you click "stop conversation", the connection is deleted from the array.
