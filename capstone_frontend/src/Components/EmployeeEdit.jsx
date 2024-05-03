import React, { useState } from 'react';

const EmployeeEdit = ({ employee, onSave, onClose }) => {
  const [formData, setFormData] = useState(employee);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSave = () => {
    onSave(formData);
  };

  return (
    <div>
      <h2>Edit Employee</h2>
      <div>
        <div>
          <input type="text" name="firstName" placeholder="First Name" value={formData.firstName} onChange={handleInputChange} /><br></br>
          <input type="text" name="lastName" placeholder="Last Name" value={formData.lastName} onChange={handleInputChange} />
        </div>
        <div>
          <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleInputChange} /><br></br>
          <input type="date" name="dateOfJoining" placeholder="Date of Joining" value={formData.dateOfJoining} onChange={handleInputChange} />
        </div>
        <div>
          <input type="text" name="grade" placeholder="Grade" value={formData.grade} onChange={handleInputChange} /><br></br>
          <button onClick={handleSave}>Save</button>
          <button onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  );
};

export default EmployeeEdit;