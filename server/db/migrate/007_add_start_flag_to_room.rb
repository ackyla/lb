class AddStartFlagToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.boolean :started, :default => false
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :started
    end
  end
end
