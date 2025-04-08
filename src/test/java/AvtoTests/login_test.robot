*** Settings ***
Library           SeleniumLibrary

*** Variables ***
${URL}            http://127.0.0.1:7000
${BROWSER}        Chrome
${USERNAME}       testuser
${PASSWORD}       testpass

*** Test Cases ***
Login With Valid Credentials
    [Documentation]    Перевірка входу користувача з правильними обліковими даними
    Open Browser To Login Page
    Input Username And Password
    Submit Login Form
    Verify Login Successful
    Close Browser

*** Keywords ***
Open Browser To Login Page
    Open Browser    ${URL}    ${BROWSER}
    Maximize Browser Window
    Wait Until Page Contains Element    id:username    5s

Input Username And Password
    Input Text    id:username    ${USERNAME}
    Input Text    id:password    ${PASSWORD}

Submit Login Form
    Click Button    id:login-btn
    Wait Until Page Contains    Welcome

Verify Login Successful
    Page Should Contain    Welcome

Close Browser
    Close Browser
