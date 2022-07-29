Specification Heading
=====================

This is an executable specification file. This file follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

## Register
tags:register
//* Go to Test Env
* Go to register page
* Sign Up

## Login the QA account
* Go to QA Env

## Forgot Password
tags:forgotPassword
* Go to Test Env
* Go to Forgot Password Page


## Change Password
tags:changePassword
* Go to Test Env
* Login with "olga.yildiz" and "qwerty12345"
* Change Password Page
* Change Password write "qwerty12345" and "Qwerty12345" and "Qwerty12345"
* LogOut
* Wrong Password Entry
* Login with "olga.yildiz" and "Qwerty12345"
* Change Password Page
* Change Password write "Qwerty12345" and "qwerty12345" and "qwerty12345"


## Place Bet
tags:placeBet
* Go to Test Env
* Login with "olga.yildiz" and "qwerty12345"
* Choose Bet
* Accept Bet
* Bet Detail View
* Bet Rearrange


## Prod Mevduat
tags:prodMevduat
* Go to QA Env
* Go to Deposit
* Make Deposit


## GET AUTH CODE FROM GMAIL AND WRITE TO ELEMENT
* Connect to gmail with "iqra.manzoor@ligastavok.ru" and "$P2LaHjT79"
* Write verification code to "emailVerificationCodeInput"
