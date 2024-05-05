import React, { useState } from 'react';

const EmployeeSearch = ({ onSearch, onClose }) => {
  const [searchParams, setSearchParams] = useState({
    firstName: '',
    lastName: '',
    email: '',
    dateOfJoining: '',
    grade: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSearch = () => {
    onSearch(searchParams);
  };

  

  return (
    <div>
      <h2>Search Employees</h2>
      <div>
        <div>
          <input type="text" name="firstName" placeholder="First Name" value={searchParams.firstName} onChange={handleInputChange} /><br></br>
          <input type="text" name="lastName" placeholder="Last Name" value={searchParams.lastName} onChange={handleInputChange} />
        </div>
        <div>
          <input type="email" name="email" placeholder="Email" value={searchParams.email} onChange={handleInputChange} /><br></br>
          <input type="date" name="dateOfJoining" placeholder="Date of Joining" value={searchParams.dateOfJoining} onChange={handleInputChange} />
        </div>
        <div>
          <input type="text" name="grade" placeholder="Grade" value={searchParams.grade} onChange={handleInputChange} /><br></br>
          <button onClick={handleSearch}>Search</button>
          <button onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  );
};

export default EmployeeSearch;