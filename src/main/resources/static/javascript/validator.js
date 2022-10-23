/**
 * A script for field validation of forms for editing and creating clients and person tuples
 */

/** All forms present on the document */
const forms = document.forms;

/** A color for a field with invalid data upon submission */
const invalidInputColor = '#AA222233';

/** The milliseconds to display an error message for invalid data fields */
const errorMessageTimeout = 4000;

/**
 * Display an error message beneath an input element for elementMessageTimeout milliseconds.
 *
 * @param {string} message The error message to display
 * @param {HTMLInputElement} field The element under which to display the error message
 */
let displayErrorMessage = (message, field) => {
    let messageBox = document.createElement('div');
    messageBox.classList.add('errorMessage');
    messageBox.style.backgroundColor = invalidInputColor;
    messageBox.innerHTML = message;

    field.parentNode.insertBefore(messageBox, field.nextSibling);

    setTimeout(() => {
        messageBox.parentNode.removeChild(messageBox);
    }, errorMessageTimeout);
};

/**
 * Check the entered value against a regex to validate user input.
 * Display an error and color the field invalidInputColor in case of invalid user input.
 *
 * @param {string} textValue The value entered by the user
 * @param {FormSubmitEvent} event The submit event for the form
 * @param {HTMLInputElement} field The input field from the form
 * @param {string} message An error message in case of invalid data entry
 * @param {regex} regex A regex to check the entered data against
 */
const checkInputField = (textValue, event, field, message, regex) => {
    let valid = textValue.match(regex);

    if (!valid) {
        event.preventDefault();
        field.style.backgroundColor = invalidInputColor;

        displayErrorMessage(message, field);
    }
};

/**
 * Add an EventListener to all input fields of a form which returns them back to their original
 * background color after an error has turned them invalidInputColor.
 *
 * @param {HTMLFormElement} form The form which all inputs should have keydown EventListeners added
 */
const addKeyDownListeners = (form) => {
    for (let field of form) {
        let color = field.style.backgroundColor;
        field.addEventListener('keydown', (event) => {
            field.style.backgroundColor = color;
        });
    }
};

/**
 * Add EventListener to the form for the submit event such that user input is validated.
 * Add an EventListener to all input fields of the form which returns them back to their original
 * background color after an error has turned them invalidInputColor.
 *
 * @param {HTMLFormElement} form The form to add the listener on.
 */
const addEventListeners = (form) => {
    addKeyDownListeners(form);

    form.addEventListener('submit', (event) => {
        for (let field of form) {
            let inputVal = field.value;
            switch (true) {
                case field.getAttribute('id') === 'firstName':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! First name must be between 1 and 50 characters, inclusive',
                        /^[a-zA-Z-]{1,50}$/
                    );
                    break;
                case field.getAttribute('id') === 'lastName':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Last name must be between 1 and 50 characters, inclusive',
                        /^[a-zA-Z-]{1,50}$/
                    );
                    break;
                case field.getAttribute('id') === 'emailAddress':
                    validInputs = checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Invalid email address! Example Email: name@gmail.com 1-50 characters',

                        /* https://regexpattern.com/email-address/ */

                        /^(?!.{50,}$)(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                    );
                    break;
                case field.getAttribute('id') === 'streetAddress':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Street address name must be between 1 and 50 characters, inclusive',
                        /^(?!.{50,}$)[0-9]+[\.a-zA-Z0-9 -]/
                    );
                    break;
                case field.getAttribute('id') === 'city':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! City name must be between 1 and 50 characters, inclusive',
                        /^[a-zA-Z -]{1,50}$/
                    );
                    break;
                case field.getAttribute('id') === 'state':
                    validInputs = checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! State must be a 2 letter abbreviation',
                        /^[a-zA-Z]{2}$/
                    );
                    break;
                case field.getAttribute('id') === 'zipCode':
                    validInputs = checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Zip code must be a 5 digit zip code!',
                        /^[0-9]{5}$/
                    );
                    break;
                case field.getAttribute('id') === 'companyName':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Please enter a valid Company Name. No more than 100 characters long',
                        /^[a-zA-Z0-9 -]{1,100}$/
                    );
                    break;
                case field.getAttribute('id') === 'website':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Please enter a valid URL!',
                        /^(?!.{100,}$)[\/:a-zA-Z0-9-\.]+\.[a-zA-Z0-9-\.]+[a-zA-Z-]{2,}[a-zA-Z0-9-\+\?\=]*/
                    );
                    break;
                case field.getAttribute('id') === 'phone':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Please enter a valid 10 digit phone number!',
                        /^[\(]{0,1}[0-9]{3}[\)]{0,1}[ -]{0,1}[0-9]{3}[ -]{0,1}[0-9]{4}$/
                    );
                    break;
                default:
                    break;
            }
        }
    });
};

if (forms.length !== 0) {
    addEventListeners(forms[0]);
}
