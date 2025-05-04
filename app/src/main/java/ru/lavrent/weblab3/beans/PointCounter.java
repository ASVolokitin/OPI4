package ru.lavrent.weblab3.beans;

import java.util.List;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import jakarta.inject.Named;
import ru.lavrent.weblab3.models.Record;

@Named("pointCounterBean")
public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean {

  private long successHits;
  private long misses;
  private long totalHitAmount;
  private long sequenceNumber = 1;

  public synchronized long getMisses() {
    return misses;
  }

  public synchronized long getSuccessHits() {
    return successHits;
  }

  public synchronized long getTotalHitAmount() {
    return totalHitAmount;
  }

  @Override
  public MBeanNotificationInfo[] getNotificationInfo() {
    String[] notifTypes = new String[] { "ru.lavrent.weblab3.dividedBy5" };
    String name = Notification.class.getName();
    String description = "Notification when a point is out of the displayed area.";
    MBeanNotificationInfo info = new MBeanNotificationInfo(notifTypes, name, description);
    return new MBeanNotificationInfo[] { info };
  }

  public synchronized void setHits(List<Record> records) {
    this.totalHitAmount = records.size();
    this.successHits = records.stream().filter(Record::isHit).count();
    this.misses = this.totalHitAmount - this.successHits;
  }

  public synchronized void countHits(float x, float y, boolean result) {
    this.totalHitAmount++;
    if (result)
      this.successHits++;
    else
      this.misses++;
    if (this.successHits % 5 == 0) {
      String msg = String.format("The current number of hits (%d) is divided by 5.", this.successHits);

      Notification notif = new Notification("ru.lavrent.weblab3.dividedBy5", this, sequenceNumber++,
          System.currentTimeMillis(),
          msg);
      sendNotification(notif);
    }
    return;
  }
  // List<Record> records = formBean.getHistoryBean().getRecords();
  // this.totalHitAmount = records.size();
  // long successHits = records.stream().filter(Record::isHit).count();
  // if (successHits % 5 == 0) System.out.println("ALARM ALARM ALARM");
  // this.successHits = successHits;
  // this.misses = this.totalHitAmount - this.successHits;
  // return this.successHits;

  // String msg = "Point (" + x + ", " + y + ") is out of the displayed area: " +
  // "[" + xMin + ", " + xMax + "] x [" + yMin + ", "
  // + yMax + "]";

  // Notification notif = new Notification("ru.ilia.demointellij.outOfBounds",
  // this, sequenceNumber++, System.currentTimeMillis(),
  // msg);
  // sendNotification(notif);

  // public void submit() {
  // formBean.submit();
  // countHits();
  // }

  // public void submitHidden() {
  // formBean.submitHidden();
  // countHits();
  // }

  public synchronized void printHits() {
    System.out.println(String.format("Hit %d times of %d.", this.successHits, this.totalHitAmount));
  }
}
