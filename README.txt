data is our "database", including user: user information, account: information related to bank accounts
dto is a data transfer object, which contains the objects from which data needs to be read.
frontened are all forms, including account creation page, user page, loan creation page, login page, registration page, deposit and withdrawal page.
The logic contains some other classes, and the bankaccount type is imported into the bankaccount object.
Service contains impl (logic and implementation), intf (interface)
impl contains user account related logic and bank account related logic
intf contains the interfaces of these two

The bankaccount class has user name, account type, account name, and balance. Account type is imported from bankaccount type. bankaccount type shows the three account types in this project

After successful login, enter the customer page. The left column includes deposit and withdrawal buttons and logout buttons. The right column displays three account types, and under each type displays all accounts of this type for the user.

Click the add button under checking and saving to enter the account creation page. The account type will be automatically selected as checking or saving according to the position of the selected button. Then enter the account name and deposit amount, successfully create and return to the customer page. Each page contains a return key

Click the add button under security to enter the security account creation page, and the account type is automatically selected as security. Security is special. It corresponds to the saving account one-to-one. From the drop-down list, you can select an account name as the security account from all saving accounts with a balance of no less than 5,000, and deposit money from this saving account into the security account. After depositing, the amount in this saving account cannot be less than 2,500.

Accounts with the same name cannot be created. For example, if there is abc in checking, then abc cannot be created in checking/saving. Security is the same and is bound to saving one by one.

A handling fee of 50 will be charged when creating an account. If it is checking/saving, it will be charged directly from the deposited money. If it is security, there will be no handling fee.

Click the logout button on the customer page to return to the login page

Click D&W on the customer page to enter the deposit and withdrawal page. First select the account type, and then display all the accounts of this type for the user. Select an account and choose deposit (+) or withdrawal (-). A 10 handling fee is automatically charged when withdrawing money. For example, withdrawing 100 is actually withdrawing 110.

If you choose a security type account, the deposit means transferring money from the corresponding saving to security (the saving balance cannot be less than 2500 after the transfer), and the withdrawal means transferring money from security to the corresponding saving (no requirements and no handling fees) .

All numeric inputs are checked for correctness, only integers and less than 10 digits can be entered