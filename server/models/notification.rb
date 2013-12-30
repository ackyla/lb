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
    end.merge(self.to_hash)
  end

  def to_hash
    hash = Hash[self.attributes]
    hash["notification_id"] = self.id
    hash.delete "id"
    hash
  end
end
