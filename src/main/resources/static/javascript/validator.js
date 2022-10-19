const forms = document.forms;
const invalidInputColor = '#AA222233';
const errorMessageTimeout = 4000;

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

const checkInputField = (textValue, event, field, message, regex) => {
    let valid = textValue.match(regex);

    if (!valid) {
        event.preventDefault();
        field.style.backgroundColor = invalidInputColor;

        displayErrorMessage(message, field);
    }
};

const addKeyDownListeners = (form) => {
    for (let field of form) {
        let color = field.style.backgroundColor;
        field.addEventListener('keydown', (event) => {
            field.style.backgroundColor = color;
        });
    }
};

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

                        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                    );
                    break;
                case field.getAttribute('id') === 'streetAddress':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Street address name must be between 1 and 50 characters, inclusive',
                        /^[0-9]+[\.a-zA-Z0-9 -]{1,50}$/
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
                        /^[\/:a-zA-Z0-9-\.]+\.[a-zA-Z0-9-\.]+[a-zA-Z-]{2,}[a-zA-Z0-9-\+\?\=]*$/
                    );
                    break;
                case field.getAttribute('id') === 'phone':
                    checkInputField(
                        inputVal,
                        event,
                        field,
                        'Error! Please enter a valid 10 digit phone number!',
                        /^[\(]*[0-9]{3}[\)]*[ -]*[0-9]{3}[ -]*[0-9]{4}$/
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
