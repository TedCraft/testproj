function deleteDepartment(empNo) {
    let http = new XMLHttpRequest();
    http.open("DELETE", "http://localhost:8080/departments/" + empNo, false);
    http.send();
}

function updateDepartment(deptNo, name, budget) {
    let http = new XMLHttpRequest();
    http.open("POST", "http://localhost:8080/departments/" + deptNo, false);
    http.setRequestHeader("Content-Type", "application/json");
    http.send(JSON.stringify({
        name: name,
        budget: budget
    }));

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if(response.message.includes("Float")) {
            alert("Wrong type of Budget!");
        }
        else {
            alert(response.message);
        }
        loadDepartments();
    }
}

function addDepartment(http) {
    let department = JSON.parse(http.responseText);
    let html = '<tr>' +
        '        <td class="editButton"><button>✎</button></td>' +
        '        <td>' + department.deptNo + '</td>\n' +
        '        <td>' + department.name + '</td>\n' +
        '        <td>' + department.budget + '</td>' +
        '        <td class="deleteButton"><button>x</button></td>' +
        '</tr>';
    document.getElementById("departmentList").innerHTML += html;
}

function createDepartment() {
    let name = document.getElementById("name").value;
    let budget = document.getElementById("budget").value;

    let http = new XMLHttpRequest();
    http.open("POST", "http://localhost:8080/departments", false);
    http.setRequestHeader("Content-Type", "application/json");
    http.send(JSON.stringify({
        name: name,
        budget: budget
    }));

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if(response.message.includes("Float")) {
            alert("Wrong type of Budget!");
        }
        else {
            alert(response.message);
        }
        return;
    }

    addDepartment(http);
}

function loadFunction(http) {
    http.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let departments = JSON.parse(this.responseText);
            let html = '<tr>\n' +
                '        <th></th>\n' +
                '        <th>Dept no</th>\n' +
                '        <th>Name</th>\n' +
                '        <th>Budget</th>\n' +
                '        <th></th>\n' +
                '</tr>';
            for (let i = 0; i < departments.length; i++) {
                let department = departments[i];
                html = html + '<tr>' +
                    '        <td class="editButton"><button>✎</button></td>' +
                    '        <td>' + department.deptNo + '</td>\n' +
                    '        <td>' + department.name + '</td>' +
                    '        <td>' + department.budget + '</td>' +
                    '        <td class="deleteButton"><button>x</button></td>' +
                    '</tr>';

            }
            document.getElementById("departmentList").innerHTML = html;
        }
    };
}

function loadDepartments() {
    let http = new XMLHttpRequest();
    loadFunction(http);
    http.open("GET", "http://localhost:8080/departments", true);
    http.send();
}

function searchByDeptNo() {
    let value = document.getElementById("dept_search_field").value;
    let http = new XMLHttpRequest();
    loadFunction(http);
    http.open("GET", "http://localhost:8080/departments/" + value, false);
    http.send();

    let response = JSON.parse(http.responseText);
    if (response.error !== undefined) {
        if (response.message.includes("Integer")) {
            alert("Wrong type of Dept No!");
        } else {
            alert(response.message);
        }
    }
}

document.getElementById("departmentList").addEventListener('click', function(evt){
    if(evt.target.closest('.deleteButton')) {
        let empNo = evt.target.closest('tr').querySelectorAll('td')[1].innerHTML;
        deleteDepartment(empNo);
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
        updateDepartment(row[1].innerHTML, row[2].innerHTML, row[3].innerHTML);
    }

})

loadDepartments();