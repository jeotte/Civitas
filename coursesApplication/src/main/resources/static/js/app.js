var app = angular.module('coursesApp', ['smart-table']);

$(function() {
	
	// Add rules for validating the 'Add Course' form
	$('#addCourseForm').submit(function(event) {
		event.preventDefault();
	}).validate({
		rules: {
			subjectInput: {required: true},
			courseNumInput: {required: true, digits: true},
			descInput: {required: true}
		},
		submitHandler: function(form) {
			// If validation is successful, submit the form
			angular.element(form).scope().saveCourse();
			return false;
		}
	});	
});

// Add the controller
app.controller('CourseController', [ '$scope', 'CourseService',
	                                  function($scope, CourseService) {
		$scope.courseDTO = {
				courseNumber: null,
				subject: null,
				description: null
		};
		
		$scope.message = null;
		
		$scope.saveCourse = function() {
				CourseService.saveCourse($scope.courseDTO)
				.then(function success(response) {
					$scope.showMessage($scope, 'Course Added', false);
					$scope.courseDTO = {
							courseNumber : null,
							subject : null,
							description : null
					};
				},
				function error(response) {
					var errorMessage = 'Error Adding Course';
					if (response.data && response.data.message) {
						errorMessage += ": " + response.data.message;
					} 
					$scope.showMessage($scope, errorMessage, true);
				});
		};
		
		$scope.deleteCourse = function(row) {
			var index = $scope.coursesCollection.indexOf(row);
			if (index !== -1) {
				CourseService.deleteCourse(row)
				.then(function success(response) {
					$scope.showMessage($scope, 'Course Deleted', false);
					
					// Remove the course from the table
					$scope.coursesCollection.splice(index, 1);
				},
				function error(response) {
					var errorMessage = 'Error Deleting Course';
					if (response.data && response.data.message) {
						errorMessage += ": " + response.data.message;
					} 
					$scope.showMessage($scope, errorMessage, true);
				});
			} else {
				$scope.showMessage($scope, 'Error Deleting Course', true);
			}
		};
		
		$scope.showMessage = function($scope, message, isError) {
			// Set the message to be displayed
			$scope.message = message;
			
			// Get the div
			var messageDiv = $("#messageDiv").removeClass().addClass("visible"),
				hideMessageDiv = function() {
					messageDiv.removeClass().addClass("hidden");
				};
			
			// If the message is an error, show the text as red, otherwise as gray/muted
			if (isError) {
				messageDiv.addClass("text-danger");
			} else {
				messageDiv.addClass("text-muted");
			}

			// Hide the message div after some time 
			setTimeout(hideMessageDiv, 10000);
		}
		
		$scope.search = function() {
			if ($scope.searchDesc) {
				CourseService.findCourses($scope.searchDesc)
				.then(function success(response) {
					$scope.coursesCollection = response.data;
					$('#coursesDiv').removeClass("hidden");
				}, function error(response) {
					var errorMessage = 'Error Searching for Course';
					if (response.data && response.data.message) {
						errorMessage += ": " + response.data.message;
					} 
					$scope.showMessage($scope, errorMessage, true);
				});
			} else {
				$scope.showMessage($scope, "The description must be provided", true);
			}
		};
}]);


// Add the service
app.service('CourseService', [ '$http', function($http) {
	this.saveCourse = function saveCourse(courseDTO) {
		return $http({
			method: 'POST',
			url: 'courses',
			data: courseDTO,
			headers: {
		        'Content-Type': 'application/json',
		    }
		})
	}
	
	this.deleteCourse = function deleteCourse(row) {
		return $http({
			method: 'DELETE',
			url: 'courses',
			data: row,
			headers: {
		        'Content-Type': 'application/json',
		    }
		})
	}
	
	this.findCourses = function findCourses(desc) {
		return $http({
			method: 'GET',
			url: 'courses?desc='+desc
		})
	}
}]);