import React, { useState, useEffect } from 'react';
import axios from 'axios';
import EmployeeTable from './Components/EmployeeTable';
import EmployeeSearch from './Components/EmployeeSearch';
import EmployeeEdit from './Components/EmployeeEdit';
import './App.css';

const ParentComponent = () => {
  const [employees, setEmployees] = useState([]);
  const [showSearch, setShowSearch] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [selectAll, setSelectAll] = useState(false);
  const [selectedRows, setSelectedRows] = useState([]);
  const [file, setFile] = useState(null);

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await axios.get('http://localhost:8085/api/v1/employees');
      setEmployees(response.data);
    } catch (error) {
      console.error('Error fetching employees:', error);
    }
  };

  const handleButtonClick = async (buttonNumber) => {
    console.log(`Button ${buttonNumber} clicked`);
    if (buttonNumber === 1) {
      fetchEmployees();
    } else if (buttonNumber === 2) {
      setShowSearch(true);
      setShowEdit(false);
    } else if (buttonNumber === 4) {
      handleExportCSV();
    }
  };

  const handleModify = (employee) => {
    setSelectedEmployee(employee);
    setShowEdit(true);
    setShowSearch(false); 
  };

  const handleSave = async (editedEmployeeData) => {
    try {
      console.log("Save", editedEmployeeData);
      await axios.put(`http://localhost:8085/api/v1/employees/${editedEmployeeData.id}`, editedEmployeeData);
      setShowEdit(false);
      fetchEmployees();
    } catch (error) {
      console.error('Error saving employee:', error);
    }
  };

  const handleCloseEdit = () => {
    setShowEdit(false);
  };

  const handleSearch = async (searchParams) => {
    try {
      const searchParamsString = new URLSearchParams(searchParams).toString();
      console.log(`Searching ${searchParamsString}`);
      const response = await axios.get(`http://localhost:8085/api/v1/employees/search?${searchParamsString}`);
      setEmployees(response.data);
    } catch (error) {
      console.error('Error searching employees:', error);
    }
  };

  const handleCloseSearch = () => {
    setShowSearch(false);
  };

  const toggleSelectAll = () => {
    setSelectAll(prevSelectAll => !prevSelectAll);
    if (!selectAll) {
      setSelectedRows(employees.map(employee => employee.id));
    } else {
      setSelectedRows([]);
    }
  };

  const toggleRowSelection = (id) => {
    if (selectedRows.includes(id)) {
      setSelectedRows(prevSelectedRows => prevSelectedRows.filter(rowId => rowId !== id));
    } else {
      setSelectedRows(prevSelectedRows => [...prevSelectedRows, id]);
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert('Please select a file.');
      return;
    }

    try {
      const reader = new FileReader();
      reader.onload = async () => {
        const csvData = reader.result;
        try {
          const response = await axios.post('http://localhost:8085/api/v1/employees/uploadCsv', csvData, {
            headers: {
              'Content-Type': 'text/plain'
            }
          });
          console.log(response.data);
          alert('CSV data uploaded successfully.');
          fetchEmployees();
        } catch (error) {
          console.error('Error uploading CSV data:', error);
          alert('Failed to upload CSV data.');
        }
      };
      reader.readAsText(file);
    } catch (error) {
      console.error('Error reading file:', error);
      alert('Failed to read file.');
    }
  };

  const handleExportCSV = () => {
    const selectedEmployees = employees.filter(employee => selectedRows.includes(employee.id));
    console.log(selectedEmployees);
    const csvData = selectedEmployees.map(employee => {
      return `${employee.id},${employee.firstName},${employee.lastName},${employee.email},${employee.dateOfJoining},${employee.grade}`;
    }).join('\n');

    console.log(csvData);
    const blob = new Blob([csvData], { type: 'text/csv' });
    console.log(blob); 
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'selected_employees.csv';
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  return (
    <div className='App'>
      <h1>Employee Registry</h1>
      <div className="button-container">
        <button className="button" onClick={() => handleButtonClick(1)}>All Employees</button>
        <button className="button" onClick={() => handleButtonClick(2)}>Search Employee</button>
        <input type="file" onChange={handleFileChange} />
        <button onClick={handleUpload}>Import CSV</button>
        <button className="button" onClick={() => handleButtonClick(4)}>Export</button>
      </div>
      {showEdit && <EmployeeEdit employee={selectedEmployee} onSave={handleSave} onClose={handleCloseEdit} />}
      {showSearch && <EmployeeSearch onSearch={handleSearch} onClose={handleCloseSearch} />}
      <EmployeeTable employees={employees} onModify={handleModify} onSelectAll={toggleSelectAll} onSelectRow={toggleRowSelection} selectedRows={selectedRows} />
    </div>
  );
};

export default ParentComponent;