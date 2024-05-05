import React, { useEffect, useState } from 'react';

const EmployeeTable = ({ employees, onModify, onSelectAll, onSelectRow, selectedRows }) => {
  const [selectAll, setSelectAll] = useState(false);

  useEffect(() => {
    setSelectAll(selectedRows.length === employees.length && employees.length > 0);
  }, [employees, selectedRows]);

  const toggleSelectAll = () => {
    const updatedSelectAll = !selectAll;
    setSelectAll(updatedSelectAll);
    onSelectAll(updatedSelectAll);
  };

  const toggleRowSelection = (id) => {
    onSelectRow(id);
  };

  useEffect(() => {
    // If all rows are selected manually, update the select all state
    if (selectedRows.length === employees.length && selectedRows.length > 0) {
      setSelectAll(true);
    } else {
      setSelectAll(false);
    }
  }, [selectedRows, employees]);

  return (
    <table>
      <thead>
        <tr>
          <th>
            <input
              type="checkbox"
              checked={selectAll}
              onChange={toggleSelectAll}
            />
          </th>
          <th>ID</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Email</th>
          <th>Date of Joining</th>
          <th>Grade</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {employees.map(employee => (
          <tr key={employee.id}>
            <td>
              <input
                type="checkbox"
                checked={selectedRows.includes(employee.id)}
                onChange={() => toggleRowSelection(employee.id)}
              />
            </td>
            <td>{employee.id}</td>
            <td>{employee.firstName}</td>
            <td>{employee.lastName}</td>
            <td>{employee.email}</td>
            <td>{employee.dateOfJoining}</td>
            <td>{employee.grade}</td>
            <td>
              <button onClick={() => onModify(employee)}>Modify</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default EmployeeTable;