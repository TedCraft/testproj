function deleteEmployee(empNo) {
    let http = new XMLHttpRequest();
    http.open("DELETE", `../employees/` + empNo, false);
    http.send();
}

function updateEmployee(empNo, deptNo, firstName, lastName, hireDate, salary) {
    let http = new XMLHttpRequest();
    http.open("POST", `../employees/` + empNo, false);
    http.setRequestHeader("Content-Type", "application/json");
    http.send(JSON.stringify({
        deptNo: deptNo,
        firstName: firstName,
        lastName: lastName,
        hireDate: hireDate,
        salary: salary
    }));

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if(response.message.includes("Date")) {
            alert("Wrong type of Hire date!");
        }
        else if(response.message.includes("Float")) {
            alert("Wrong type of Salary!");
        }
        else {
            alert(response.message);
        }
        loadEmployees();
    }
}

function addEmployee(http) {
    let employee = JSON.parse(http.responseText);
    let html = '<tr>' +
        '        <td class="editButton"><button>✎</button></td>' +
        '        <td>' + employee.empNo + '</td>\n' +
        '        <td>' + employee.deptNo + '</td>\n' +
        '        <td>' + employee.firstName + '</td>\n' +
        '        <td>' + employee.lastName + '</td>' +
        '        <td>' + employee.hireDate + '</td>' +
        '        <td>' + employee.salary + '</td>' +
        '        <td class="deleteButton"><button>x</button></td>' +
        '</tr>';
    document.getElementById("employeeList").innerHTML += html;
}

function createEmployee() {
    let deptNo = document.getElementById("deptNo").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let hireDate = document.getElementById("hireDate").value;
    let salary = document.getElementById("salary").value;

    let http = new XMLHttpRequest();
    http.open("POST", `../employees`, false);
    http.setRequestHeader("Content-Type", "application/json");
    http.send(JSON.stringify({
        deptNo: deptNo,
        firstName: firstName,
        lastName: lastName,
        hireDate: hireDate,
        salary: salary
    }));

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if(response.message.includes("Date")) {
            alert("Wrong type of Hire date!");
        }
        else if(response.message.includes("Float")) {
            alert("Wrong type of Salary!");
        }
        else {
            alert(response.message);
        }
        return;
    }

    addEmployee(http);
}

function loadFunction(http) {
    http.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let employees = JSON.parse(this.responseText);
            let html = '<tr>\n' +
                '        <th></th>\n' +
                '        <th>Emp no</th>\n' +
                '        <th>Dept no</th>\n' +
                '        <th>First name</th>\n' +
                '        <th>Last name</th>\n' +
                '        <th>Hire date</th>\n' +
                '        <th>Salary</th>\n' +
                '        <th></th>\n' +
                '</tr>';
            for (let i = 0; i < employees.length; i++) {
                let employee = employees[i];
                html = html + '<tr>' +
                    '        <td class="editButton"><button>✎</button></td>' +
                    '        <td>' + employee.empNo + '</td>\n' +
                    '        <td>' + employee.deptNo + '</td>\n' +
                    '        <td>' + employee.firstName + '</td>\n' +
                    '        <td>' + employee.lastName + '</td>' +
                    '        <td>' + employee.hireDate + '</td>' +
                    '        <td>' + employee.salary + '</td>' +
                    '        <td class="deleteButton"><button>x</button></td>' +
                    '</tr>';

            }
            document.getElementById("employeeList").innerHTML = html;
        }
    };
}

function loadEmployees() {
    let http = new XMLHttpRequest();
    loadFunction(http);
    http.open("GET", `../employees`, true);
    http.send();
}

function searchBy() {
    let searchOption = document.getElementById('search').value;
    let value = document.getElementById("emp_search_field").value;
    let http = new XMLHttpRequest();
    loadFunction(http);
    switch (searchOption) {
        case "Emp No":
            http.open("GET", `../employees/` + value, false);
            break;
        case "Dept No":
            http.open("GET", `../departments/` + value + "/employees", false);
            break;
        case "Full Name":
            let fullName = value.split(" ");
            if(fullName[0] === "") {
                alert("Enter the First name!");
                return;
            }
            else if(fullName[1] === "") {
                alert("Enter the Last name!");
                return;
            }
            http.open("GET", `../employees/?name=` + fullName[0] + "&lastname=" + fullName[1], false);
            break;
        default:
            return;
    }
    http.send();

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if (response.message.includes("Integer")) {
            alert("Wrong type of " + searchOption + "!");
        }
        else {
            alert(response.message);
        }
    }
}

document.getElementById("employeeList").addEventListener('click', function(evt){
    if(evt.target.closest('.deleteButton')) {
        let empNo = evt.target.closest('tr').querySelectorAll('td')[1].innerHTML;
        deleteEmployee(empNo);
        evt.target.closest('tr').remove();
    }
    if(evt.target.closest('.editButton')) {
        let row = evt.target.closest('tr').querySelectorAll('td');
        let button = document.createElement('button');
        button.innerHTML = "✓";
        row[0].setAttribute("class", "applyButton");
        row[0].innerHTML = "";
        row[0].appendChild(button);

        for(let i = 2; i<row.length - 1; i++) {
            let input = document.createElement('input');
            input.value = row[i].innerHTML;
            row[i].innerHTML = '';
            row[i].appendChild(input);
        }
    }
    if(evt.target.closest('.applyButton')) {
        let row = evt.target.closest('tr').querySelectorAll('td');
        let button = document.createElement('button');
        button.innerHTML = "✎";
        row[0].setAttribute("class", "editButton");
        row[0].innerHTML = "";
        row[0].appendChild(button);

        for(let i = 2; i<row.length-1; i++) {
            row[i].innerHTML = row[i].children[0].value;
        }
        updateEmployee(row[1].innerHTML, row[2].innerHTML, row[3].innerHTML, row[4].innerHTML, row[5].innerHTML, row[6].innerHTML);
    }

})

loadEmployees();