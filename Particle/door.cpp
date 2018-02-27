#include "door.h"


DoorControl::DoorControl()
{
  _errorTimer = new Timer(60 * 1000, error, true);
  _autoShutTimer = new Timer(60 * 1000, autoShut, true);
  _closeSwitch = digitalRead(CLOSE_REED) == HIGH;
  _openSwitch = digitalRead(OPEN_REED) == HIGH;
}
void read()
{
  _closeSwitch = digitalRead(CLOSE_REED) == HIGH;
  _openSwitch = digitalRead(OPEN_REED) == HIGH;
}

void DoorControl::Open()
{
    digitalWrite(DOOR, HIGH);
    delay(500);
    digitalWrite(DOOR, LOW);
}

void DoorControl::Close()
{
    digitalWrite(DOOR, HIGH);
    delay(500);
    digitalWrite(DOOR, LOW);
}

void DoorControl::OnError()
{
  _failedToShut = true;
}

void DoorControl::AutoShut()
{
  _timeToShut -= 1;
  if (_timeToShut == 0)
  {
    _autoShutTimer.stopFromISR();
    toggle("");
  }
}

void override()
{
  if (_openSwitch)
  {
    _overridden = !_overridden;
    if (_overridden)
    {
      _autoShutTimer.stopFromISR();
    }
    else
    {
      _timeToShut = _autoShutTimeOut;
      _autoShutTimer.startFromISR();
    }
  }
}


void updateState()
{
  bool closeSwitch = digitalRead(CLOSE_REED) == HIGH;
  bool openSwitch = digitalRead(OPEN_REED) == HIGH;

  //This is an error state. Should never happen
  if (closeSwitch && openSwitch)
  {
    _autoShutTimer.stop();
    _errorTimer.stop();
    _openSwitch = openSwitch;
    _closeSwitch = closeSwitch;
    return;
  }
  //If the open switch just toggled
  if (_openSwitch != openSwitch)
  {
    _failedToShut = false;
    _openSwitch = openSwitch;
    _overridden = false;
    //Door has opened
    if (_openSwitch)
    {
      //Enable AutoShut Timer
      _timeToShut = _autoShutTimeOut;
      _autoShutTimer.startFromISR();
      //Disable Error Timer
      _errorTimer.stopFromISR();
      Particle.publish("Door Opened");
    }
    //Door is closing
    else
    {
      //Enable Error Timer
      _errorTimer.startFromISR();
      Particle.publish("Door Closing");
    }
  }
  //If the close switch just toggled
  if (_closeSwitch != closeSwitch)
  {
    _failedToShut = false;
    _closeSwitch = closeSwitch;
    //Door has closed
    if (_closeSwitch)
    {
      //Disable Error Timer
      _errorTimer.stopFromISR();
      Particle.publish("Door Closed");
    }
    //Door is opening
    else
    {
      //Enable Error Timer
      _errorTimer.startFromISR();
      Particle.publish("Door Opening");
    }
}
