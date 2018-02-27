bool _openSwitch = false;
bool _closeSwitch = false;
int _autoShutTimeOut = 5;
int _timeToShut = 5;
bool _failedToShut = false;
bool _overridden = false;

enum ErrorCode
{
  ErrorDidNotRespond, //The door was toggled, but the state of the reeds did not change.
  ErrorDidNotClose, //Door started to close, but the second reed switch never engaged
  ErrorDidNotOpen,//Door started to open, but the second reed switch never engaged
  ErrorReClosed, //Door started to open, then closed again
  ErrorReOpened, //Door started to close, then opened again
  ErrorUnknown, // Both reed switches are engaged
}
enum DoorState
{
  Opened,
  Closed,
  Opening,
  Closing,
  Error
}

class DoorControl
{
public:
  DoorControl(int autoShutTimeout);

  DoorState GetDoorState() { return _state;}
  ErrorCode GetErrorCode() { return _error;}

  void Open();
  void Close();
  void Override();
private:
  void OnError();
  void AutoShut();

  int autoShutTimeout;
  DoorState _state;
  ErrorCode _error;
  Timer * _errorTimer;
  Timer * _autoShutTimer;
}
