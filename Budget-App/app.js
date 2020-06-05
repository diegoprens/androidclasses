const reasonInput = document.querySelector('#input-reason');
const amountInput = document.querySelector('#input-amount');
const clearBtn = document.querySelector('#btn-clear');
const addBtn = document.querySelector('#btn-add');
const expensesList = document.querySelector('#expenses-list');
const totalExpensesOutput = document.querySelector('#total-expenses');
const alertCtrl = document.querySelector('ion-alert-controller');
let total = 0;

const clearForm = () => {
    reasonInput.value = "";
    amountInput.value = "";
};

clearBtn.addEventListener('click', clearForm);

addBtn.addEventListener('click', () => {
    console.log("Working...");
    const reasonValue = reasonInput.value;
    const amountValue = amountInput.value;

    //console.log(reasonValue.trim().length);
    if(
        reasonValue.trim().length <= 0 ||
        amountValue <= 0 ||
        amountValue.trim().length <= 0
    ) {
        const alert = document.createElement('ion-alert');
        alert.header = 'Entradas Invalidas';
        alert.message = 'Por favor, digite una razón y cantidad valida!';
        alert.buttons = ['OK'];
        document.body.appendChild(alert);
        alert.present();
        
        return;
    }

    const newItem = document.createElement('ion-item');
    newItem.textContent = reasonValue + ': $ ' + amountValue;
    
    expensesList.appendChild(newItem);

    total += +amountValue;
    totalExpensesOutput.textContent = '$ ' + total;
    //console.log(reasonValue + ' - ' + amountValue);
    clearForm();
});
