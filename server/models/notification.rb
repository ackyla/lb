class Notification < ActiveRecord::Base
  belongs_to :user
  belongs_to :location
  belongs_to :territory
  scope :unread, conditions: {read: false}

  def notification_info
    case self.notification_type
    when "entering"
      {location: self.location.to_hash}
    when "detection"
      {territory: self.territory.to_hash}
    end.merge(
      notification_id: self.id,
      user: self.user.to_hash
      )
  end
end
