USE master
GO
CREATE DATABASE MovieAppProject
GO
USE MovieAppProject
GO

-- tables

CREATE TABLE UserRole
(
	IDUserRole INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(50) CONSTRAINT UQ_UserRole_Name UNIQUE NOT NULL,
)
GO

CREATE TABLE AppUser
(
	IDAppUser INT PRIMARY KEY IDENTITY,
	Username NVARCHAR(50) CONSTRAINT UQ_AppUser_Username UNIQUE NOT NULL,
	Password NVARCHAR(50) NOT NULL,
	UserRoleID INT CONSTRAINT FK_AppUser_UserRole FOREIGN KEY REFERENCES UserRole(IDUserRole) NOT NULL,
)
GO

CREATE TABLE Movie
(
	IDMovie INT PRIMARY KEY IDENTITY,
	Title NVARCHAR(300) NOT NULL,
	PubDate NVARCHAR(100),
	Description NVARCHAR(900),
	OriginalTitle NVARCHAR(300),
	Duration INT,
	Poster NVARCHAR(300),
	Link NVARCHAR(300),
	Guid NVARCHAR(300),
	Trailer NVARCHAR(300),
	StartDate NVARCHAR(100)
)
GO

CREATE TABLE Director
(
	IDDirector INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(50) NOT NULL,
	LastName NVARCHAR(50) NOT NULL,
	CONSTRAINT UQ_Director UNIQUE (FirstName, LastName)
)
GO

CREATE TABLE Actor
(
	IDActor INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(50) NOT NULL,
	LastName NVARCHAR(50) NOT NULL,
	CONSTRAINT UQ_Actor UNIQUE (FirstName, LastName)
)
GO

CREATE TABLE Genre
(
	IDGenre INT PRIMARY KEY IDENTITY,
	Name NVARCHAR(50) CONSTRAINT UQ_Genre_Name UNIQUE NOT NULL,
)
GO

CREATE TABLE MovieDirector
(
	IDMovieDirector INT PRIMARY KEY IDENTITY,
	MovieID INT CONSTRAINT FK_MovieDirector_Movie FOREIGN KEY REFERENCES Movie(IDMovie),
	DirectorID INT CONSTRAINT FK_MovieDirector_Director FOREIGN KEY REFERENCES Director(IDDirector),
	CONSTRAINT UQ_MovieDirector UNIQUE (MovieID, DirectorID)
)
GO

CREATE TABLE MovieActor
(
	IDMovieActor INT PRIMARY KEY IDENTITY,
	MovieID INT CONSTRAINT FK_MovieActor_Movie FOREIGN KEY REFERENCES Movie(IDMovie) NOT NULL,
	ActorID INT CONSTRAINT FK_MovieActor_Actor FOREIGN KEY REFERENCES Actor(IDActor) NOT NULL,
	CONSTRAINT UQ_MovieActor UNIQUE (MovieID, ActorID)
)
GO

CREATE TABLE MovieGenre
(
	IDMovieGenre INT PRIMARY KEY IDENTITY,
	MovieID INT CONSTRAINT FK_MovieGenre_Movie FOREIGN KEY REFERENCES Movie(IDMovie),
	GenreID INT CONSTRAINT FK_MovieGenre_Genre FOREIGN KEY REFERENCES Genre(IDGenre),
	CONSTRAINT UQ_MovieGenre UNIQUE (MovieID, GenreID)
)
GO

-- procedures

-- AppUser

CREATE PROCEDURE createAppUser
	@Username NVARCHAR(50),
	@Password NVARCHAR(50),
	@UserRoleID INT,
	@ID INT OUTPUT
AS 
BEGIN 
	INSERT INTO AppUser VALUES(@Username, @Password, @UserRoleID)
	SET @ID = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateAppUser
	@Username NVARCHAR(50),
	@Password NVARCHAR(50),
	@UserRoleID INT,
	@Id INT 
AS 
BEGIN 
	UPDATE AppUser 
	SET 
		Username = @Username,
		Password = @Password,
		UserRoleID = @UserRoleID
	WHERE IDAppUser = @Id
END
GO

CREATE PROCEDURE deleteAppUser
	@Id INT	 
AS 
BEGIN 
	DELETE FROM AppUser WHERE IDAppUser = @Id
END
GO

CREATE PROCEDURE selectAppUser
	@ID INT
AS 
BEGIN 
	SELECT * FROM AppUser WHERE IDAppUser = @ID
END
GO

CREATE PROCEDURE selectAppUsers
AS 
BEGIN 
	SELECT * FROM AppUser
END
GO

-- UserRole

CREATE PROCEDURE createUserRole
	@Name NVARCHAR(50),
	@ID INT OUTPUT
AS 
BEGIN 
	INSERT INTO UserRole VALUES(@Name)
	SET @ID = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateUserRole
	@Name NVARCHAR(50),
	@Id INT 
AS 
BEGIN 
	UPDATE UserRole 
	SET 
		Name = @Name
	WHERE IDUserRole = @Id
END
GO

CREATE PROCEDURE deleteUserRole
	@Id INT	 
AS 
BEGIN 
	DELETE FROM UserRole WHERE IDUserRole = @Id
END
GO

CREATE PROCEDURE selectUserRole
	@ID INT
AS 
BEGIN 
	SELECT * FROM UserRole WHERE IDUserRole = @ID
END
GO

CREATE PROCEDURE selectUserRoles
AS 
BEGIN 
	SELECT * FROM UserRole
END
GO

-- Movie

CREATE PROCEDURE createMovie
	@Title NVARCHAR(300),
	@PubDate NVARCHAR(100),
	@Description NVARCHAR(900),
	@OriginalTitle NVARCHAR(300),
	@Duration INT,
	@Poster NVARCHAR(300),
	@Link NVARCHAR(300),
	@Guid NVARCHAR(300),
	@Trailer NVARCHAR(300),
	@StartDate NVARCHAR(100),
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO Movie VALUES(@Title, @PubDate, @Description, @OriginalTitle, @Duration, @Poster, @Link, @Guid, @Trailer, @StartDate)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateMovie
	@Title NVARCHAR(300),
	@PubDate NVARCHAR(100),
	@Description NVARCHAR(900),
	@OriginalTitle NVARCHAR(300),
	@Duration INT,
	@Poster NVARCHAR(300),
	@Link NVARCHAR(300),
	@Guid NVARCHAR(300),
	@Trailer NVARCHAR(300),
	@StartDate NVARCHAR(100),
	@Id INT 
AS 
BEGIN 
	UPDATE Movie 
	SET 
		Title = @Title,
		PubDate = @PubDate,
		Description = @Description,
		OriginalTitle = @OriginalTitle,
		Duration = @Duration,
		Poster = @Poster,
		Link = @Link,
		Guid = @Guid,
		Trailer = @Trailer,
		StartDate = @StartDate
	WHERE IDMovie = @Id
END
GO

CREATE PROCEDURE deleteMovie
	@Id INT	 
AS 
BEGIN 
	DELETE FROM MovieDirector WHERE MovieID = @Id
	DELETE FROM MovieActor WHERE MovieID = @Id
	DELETE FROM MovieGenre WHERE MovieID = @Id
	DELETE FROM Movie WHERE IDMovie = @Id
END
GO

CREATE PROCEDURE selectMovie
	@Id INT
AS 
BEGIN 
	SELECT * FROM Movie WHERE IDMovie = @Id
END
GO

CREATE PROCEDURE selectMovies
AS 
BEGIN 
	SELECT * FROM Movie
END
GO

-- Director

CREATE PROCEDURE createDirector
	@FirstName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO Director VALUES(@FirstName, @LastName)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateDirector
	@FirstName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Id INT 
AS 
BEGIN 
	UPDATE Director 
	SET 
		FirstName = @FirstName,
		LastName = @LastName
	WHERE IDDirector = @Id
END
GO

CREATE PROCEDURE deleteDirector
	@Id INT	 
AS 
BEGIN 
	DELETE FROM MovieDirector where DirectorID = @Id
	DELETE FROM Director WHERE IDDirector = @Id
END
GO

CREATE PROCEDURE selectDirector
	@Id INT
AS 
BEGIN 
	SELECT * FROM Director WHERE IDDirector = @Id
END
GO

CREATE PROCEDURE selectDirectors
AS 
BEGIN 
	SELECT * FROM Director
END
GO

-- Actor

CREATE PROCEDURE createActor
	@FirstName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO Actor VALUES(@FirstName, @LastName)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateActor
	@FirstName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Id INT 
AS 
BEGIN 
	UPDATE Actor 
	SET 
		FirstName = @FirstName,
		LastName = @LastName
	WHERE IDActor = @Id
END
GO

CREATE PROCEDURE deleteActor
	@Id INT	 
AS 
BEGIN 
	DELETE FROM MovieActor where ActorID = @Id
	DELETE FROM Actor WHERE IDActor = @Id
END
GO

CREATE PROCEDURE selectActor
	@Id INT
AS 
BEGIN 
	SELECT * FROM Actor WHERE IDActor = @Id
END
GO

CREATE PROCEDURE selectActors
AS 
BEGIN 
	SELECT * FROM Actor
END
GO

-- Genre

CREATE PROCEDURE createGenre
	@Name NVARCHAR(50),
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO Genre VALUES(@Name)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateGenre
	@Name NVARCHAR(50),
	@Id INT 
AS 
BEGIN 
	UPDATE Genre 
	SET 
		Name = @Name
	WHERE IDGenre = @Id
END
GO

CREATE PROCEDURE deleteGenre
	@Id INT	 
AS 
BEGIN 
	DELETE FROM Genre WHERE IDGenre = @Id
END
GO

CREATE PROCEDURE selectGenre
	@Id INT
AS 
BEGIN 
	SELECT * FROM Genre WHERE IDGenre = @Id
END
GO

CREATE PROCEDURE selectGenres
AS 
BEGIN 
	SELECT * FROM Genre
END
GO

-- MovieDirector

CREATE PROCEDURE createMovieDirector
	@MovieID int,
	@DirectorID int,
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO MovieDirector VALUES(@MovieID, @DirectorID)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateMovieDirector
	@MovieID int,
	@DirectorID int,
	@Id INT OUTPUT
AS 
BEGIN 
	UPDATE MovieDirector
	SET
		MovieID = @MovieID,
		DirectorID = @DirectorID
	WHERE IDMovieDirector = @Id
END
GO

CREATE PROCEDURE deleteMovieDirectorsByMovieID
	@MovieID INT	 
AS 
BEGIN 
	DELETE FROM MovieDirector WHERE MovieID = @MovieID
END
GO

CREATE PROCEDURE selectMovieDirector
	@Id INT
AS 
BEGIN 
	SELECT * FROM MovieDirector WHERE IDMovieDirector = @Id
END
GO

CREATE PROCEDURE selectDirectorsForMovie
	@MovieId INT
AS 
BEGIN 
	SELECT DirectorID FROM MovieDirector WHERE MovieID = @MovieId
END
GO

CREATE PROCEDURE selectMoviesForDirector
	@DirectorId INT
AS 
BEGIN 
	SELECT MovieID FROM MovieDirector WHERE DirectorID = @DirectorId
END
GO

CREATE PROCEDURE selectMovieDirectors
AS 
BEGIN 
	SELECT * FROM MovieDirector
END
GO

-- MovieActor

CREATE PROCEDURE createMovieActor
	@MovieID int,
	@ActorID int,
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO MovieActor VALUES(@MovieID, @ActorID)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateMovieActor
	@MovieID int,
	@ActorID int,
	@Id INT OUTPUT
AS 
BEGIN 
	UPDATE MovieActor
	SET
		MovieID = @MovieID,
		ActorID = @ActorID
	WHERE IDMovieActor = @Id
END
GO

CREATE PROCEDURE deleteMovieActorsByMovieID
	@MovieID INT	 
AS 
BEGIN 
	DELETE FROM MovieActor WHERE MovieID = @MovieID
END
GO

CREATE PROCEDURE selectMovieActor
	@Id INT
AS 
BEGIN 
	SELECT * FROM MovieActor WHERE IDMovieActor = @Id
END
GO

CREATE PROCEDURE selectActorsForMovie
	@MovieId INT
AS 
BEGIN 
	SELECT ActorID FROM MovieActor WHERE MovieID = @MovieId
END
GO

CREATE PROCEDURE selectMoviesForActor
	@ActorId INT
AS 
BEGIN 
	SELECT MovieID FROM MovieActor WHERE ActorID = @ActorId
END
GO

CREATE PROCEDURE selectMovieActors
AS 
BEGIN 
	SELECT * FROM MovieActor
END
GO

-- MovieGenre

CREATE PROCEDURE createMovieGenre
	@MovieID int,
	@GenreID int,
	@Id INT OUTPUT
AS 
BEGIN 
	INSERT INTO MovieGenre VALUES(@MovieID, @GenreID)
	SET @Id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE updateMovieGenre
	@MovieID int,
	@GenreID int,
	@Id INT OUTPUT
AS 
BEGIN 
	UPDATE MovieGenre
	SET
		MovieID = @MovieID,
		GenreID = @GenreID
	WHERE IDMovieGenre = @Id
END
GO

CREATE PROCEDURE deleteMovieGenreByMovieID
	@MovieID INT	 
AS 
BEGIN 
	DELETE FROM MovieGenre WHERE MovieID = @MovieID
END
GO

CREATE PROCEDURE selectMovieGenre
	@Id INT
AS 
BEGIN 
	SELECT * FROM MovieGenre WHERE IDMovieGenre = @Id
END
GO

CREATE PROCEDURE selectGenresForMovie
	@MovieId INT
AS 
BEGIN 
	SELECT GenreID FROM MovieGenre WHERE MovieID = @MovieId
END
GO

CREATE PROCEDURE selectMoviesForGenre
	@GenreId INT
AS 
BEGIN 
	SELECT MovieID FROM MovieGenre WHERE GenreID = @GenreId
END
GO

CREATE PROCEDURE selectMovieGenres
AS 
BEGIN 
	SELECT * FROM MovieGenre
END
GO

declare @id int
exec createUserRole 'ADMINISTRATOR', @id
exec createUserRole 'BASIC', @id
exec createAppUser 'admin', 'admin', 1, @id
GO